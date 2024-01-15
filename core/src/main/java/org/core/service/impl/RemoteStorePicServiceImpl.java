package org.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.core.common.constant.PicTypeConstant;
import org.core.common.constant.defaultinfo.DefaultInfo;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.HttpRequestConfig;
import org.core.model.MiddleTypeModel;
import org.core.mybatis.iservice.TbMiddlePicService;
import org.core.mybatis.iservice.TbPicService;
import org.core.mybatis.pojo.TbMiddlePicPojo;
import org.core.mybatis.pojo.TbPicPojo;
import org.core.oss.service.OSSService;
import org.core.service.RemoteStorageService;
import org.core.service.RemoteStorePicService;
import org.core.utils.ImageTypeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RemoteStorePicServiceImpl implements RemoteStorePicService {
    
    private static final Object lock = new Object();
    
    private final TbMiddlePicService middlePicService;
    
    private final Cache<MiddleTypeModel, Long> picMiddleCache;
    
    private final Cache<Long, TbPicPojo> picCache;
    
    private final TbPicService tbPicService;
    
    private final DefaultInfo defaultInfo;
    
    private final RemoteStorageService remoteStorageService;
    
    private final HttpRequestConfig httpRequestConfig;
    
    private final OSSService ossService;
    
    
    /**
     * 封面
     *
     * @param middleIds 封面ID
     * @param type      关联ID类型
     * @return 封面地址
     */
    @Override
    public Map<Long, String> getPicUrl(Collection<Long> middleIds, Byte type) {
        Map<Long, String> picUrl = this.getPicPath(middleIds, type);
        return getPicUrlList(picUrl, false);
    }
    
    
    public Map<Long, String> getPicUrlList(Map<Long, String> paths, boolean refresh) {
        for (Map.Entry<Long, String> longStringEntry : paths.entrySet()) {
            String s;
            if (StringUtils.startsWithIgnoreCase(longStringEntry.getValue(), "http")) {
                s = longStringEntry.getValue();
            } else {
                s = remoteStorageService.getAddresses(longStringEntry.getValue(), refresh);
            }
            paths.put(longStringEntry.getKey(), s);
        }
        return paths;
    }
    
    /**
     * 封面
     *
     * @param middleIds 封面中间ID
     * @param type      关联ID类型
     * @return 封面地址
     */
    @Override
    public Map<Long, String> getPicPath(Collection<Long> middleIds, Byte type) {
        if (CollUtil.isEmpty(middleIds)) {
            return Collections.emptyMap();
        }
        final Byte finalQueryType;
        // 通过关联ID获取封面ID, 没有则全部查询
        List<MiddleTypeModel> middleTypeModels = new ArrayList<>();
        if (Objects.isNull(type)) {
            List<TbMiddlePicPojo> list = middlePicService.list(Wrappers.<TbMiddlePicPojo>lambdaQuery().in(TbMiddlePicPojo::getMiddleId, middleIds));
            if (CollUtil.isNotEmpty(list)) {
                finalQueryType = Optional.ofNullable(list.get(0)).orElse(new TbMiddlePicPojo()).getType();
                middleTypeModels.addAll(middleIds.parallelStream().map(aLong -> new MiddleTypeModel(aLong, finalQueryType)).toList());
            } else {
                finalQueryType = null;
            }
        } else {
            middleTypeModels.addAll(middleIds.parallelStream().map(aLong -> new MiddleTypeModel(aLong, type)).toList());
            finalQueryType = type;
        }
        Map<MiddleTypeModel, Long> picMiddle = picMiddleCache.getAll(middleTypeModels, aLong -> {
            List<TbMiddlePicPojo> list = middlePicService.list();
            return list.stream().collect(Collectors.toMap(o -> new MiddleTypeModel(o.getMiddleId(), o.getType()), TbMiddlePicPojo::getPicId));
        });
        // 没有查询到，直接返回默认地址
        if (CollUtil.isEmpty(picMiddle)) {
            return middleIds.parallelStream().collect(Collectors.toMap(Long::longValue, aLong -> getDefaultPicUrl(finalQueryType), (s, s2) -> s2));
        }
        // 获取缓存中地址
        Collection<Long> picIds = picMiddle.values();
        Map<Long, TbPicPojo> map = picCache.getAll(picIds, picId -> {
            List<TbMiddlePicPojo> list = middlePicService.list(Wrappers.<TbMiddlePicPojo>lambdaQuery()
                                                                       .in(TbMiddlePicPojo::getMiddleId, middleIds)
                                                                       .in(TbMiddlePicPojo::getPicId, picId)
                                                                       .eq(TbMiddlePicPojo::getType, finalQueryType));
            List<TbPicPojo> tbPicPojoList = tbPicService.listByIds(list.parallelStream().map(TbMiddlePicPojo::getPicId).collect(Collectors.toSet()));
            return tbPicPojoList.parallelStream().map(tbPicPojo -> {
                tbPicPojo = tbPicPojo == null ? new TbPicPojo() : tbPicPojo;
                if (StringUtils.isEmpty(tbPicPojo.getPath())) {
                    tbPicPojo.setPath(getDefaultPicUrl(finalQueryType));
                }
                return tbPicPojo;
            }).collect(Collectors.toMap(TbPicPojo::getId, tbPicPojo -> tbPicPojo));
        });
        // 遍历ID，如果没有查找到，则返回默认数据
        return middleIds.parallelStream().collect(Collectors.toMap(o -> o, aLong -> {
            Long picId = picMiddle.get(new MiddleTypeModel(aLong, finalQueryType));
            return picId == null ? getDefaultPicUrl(finalQueryType) : map.get(picId).getPath();
        }, (s, s2) -> s2));
    }
    
    private String getDefaultPicUrl(Byte type) {
        return switch (Optional.ofNullable(type).orElse((byte) -1)) {
            case PicTypeConstant.MUSIC -> defaultInfo.getPic().getMusicPic();
            case PicTypeConstant.PLAYLIST -> defaultInfo.getPic().getPlayListPic();
            case PicTypeConstant.ALBUM -> defaultInfo.getPic().getAlbumPic();
            case PicTypeConstant.ARTIST -> defaultInfo.getPic().getArtistPic();
            case PicTypeConstant.USER_AVATAR -> defaultInfo.getPic().getUserAvatarPic();
            case PicTypeConstant.USER_BACKGROUND -> defaultInfo.getPic().getUserBackgroundPic();
            default -> defaultInfo.getPic().getDefaultPic();
        };
    }
    
    
    /**
     * 保存封面
     *
     * @param id   添加封面关联ID,
     * @param type 添加ID类型 歌曲，专辑，歌单，歌手
     * @param pojo 封面数据
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdatePic(Long id, Byte type, TbPicPojo pojo) {
        // 查询封面是否存在, 不存在则创建。存在则创建中间表关联
        Wrapper<TbPicPojo> eq = Wrappers.<TbPicPojo>lambdaQuery().eq(TbPicPojo::getMd5, pojo.getMd5());
        synchronized (lock) {
            TbPicPojo one = tbPicService.getOne(eq);
            TbMiddlePicPojo entity = new TbMiddlePicPojo();
            
            // 保存前先删除中间表
            LambdaQueryWrapper<TbMiddlePicPojo> middlePicEq = Wrappers.<TbMiddlePicPojo>lambdaQuery()
                                                                      .eq(TbMiddlePicPojo::getMiddleId, id)
                                                                      .eq(TbMiddlePicPojo::getType, type);
            TbMiddlePicPojo middlePic = middlePicService.getOne(middlePicEq);
            if (Objects.nonNull(middlePic)) {
                TbPicPojo picPojo = tbPicService.getById(middlePic.getPicId());
                picPojo.setCount(picPojo.getCount() - 1);
                // 如果原来的封面没有关联的数据则删除
                if (picPojo.getCount() <= 0) {
                    this.removePicById(picPojo.getId());
                } else {
                    tbPicService.updateById(picPojo);
                }
                middlePicService.removeById(middlePic);
            }
            if (one == null) {
                // 没有图片数据则新建
                pojo.setCount(1);
                tbPicService.save(pojo);
                entity.setMiddleId(id);
                entity.setType(type);
                entity.setPicId(pojo.getId());
            } else {
                // 有图片数据则，添加关联图片数据
                Wrapper<TbMiddlePicPojo> lambdaQuery = Wrappers.<TbMiddlePicPojo>lambdaQuery()
                                                               .eq(TbMiddlePicPojo::getPicId, one.getId())
                                                               .eq(TbMiddlePicPojo::getMiddleId, id)
                                                               .eq(TbMiddlePicPojo::getType, type);
                TbMiddlePicPojo one1 = middlePicService.getOne(lambdaQuery);
                entity = one1 == null ? new TbMiddlePicPojo() : one1;
                entity.setMiddleId(id);
                entity.setType(type);
                entity.setPicId(one.getId());
                // 更新关联图片数
                one.setCount(one.getCount() + 1);
                // 更新封面
                tbPicService.updateById(one);
            }
            middlePicService.saveOrUpdate(entity);
            // 清除添加数据的缓存
            picMiddleCache.invalidate(new MiddleTypeModel(entity.getMiddleId(), entity.getType()));
        }
    }
    
    
    /**
     * 保存或更新封面
     *
     * @param id   添加封面关联ID,
     * @param type 添加ID类型 歌曲，专辑，歌单，歌手
     * @param url  封面数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdatePicUrl(Long id, Byte type, String url) {
        if (StringUtils.isBlank(url)) {
            return;
        }
        // 下载封面, 保存文件名为md5
        String randomName = System.currentTimeMillis() + String.valueOf(RandomUtils.nextLong());
        String dirPath = httpRequestConfig.getTempPath() + FileUtil.FILE_SEPARATOR + randomName;
        String md5Hex;
        String upload;
        File rename = null;
        File fileFromUrl;
        File touch = FileUtil.touch(dirPath);
        if (HttpUtil.isHttp(url) || HttpUtil.isHttps(url)) {
            fileFromUrl = HttpUtil.downloadFileFromUrl(url, touch, httpRequestConfig.getTimeout());
        } else {
            byte[] bytes = Base64.getDecoder().decode(url.getBytes());
            fileFromUrl = FileUtil.writeBytes(bytes, touch);
        }
        try (FileInputStream fis = new FileInputStream(fileFromUrl)) {
            md5Hex = DigestUtil.md5Hex(fileFromUrl);
            rename = FileUtil.rename(fileFromUrl, md5Hex + ImageTypeUtils.getPicType(fis), false, true);
            // 上传封面
            upload = remoteStorageService.uploadPicFile(rename, md5Hex);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BaseException(ResultCode.IMG_DOWNLOAD_ERROR);
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        } finally {
            if (rename != null) {
                log.debug("删除缓存文件{}", rename.getName());
                FileUtil.del(rename);
            }
        }
        TbPicPojo pojo = new TbPicPojo();
        pojo.setMd5(md5Hex);
        pojo.setPath(upload);
        this.saveOrUpdatePic(id, type, pojo);
    }
    
    /**
     * 删除图片
     *
     * @param ids 封面Id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removePicById(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        List<TbMiddlePicPojo> list = middlePicService.list(Wrappers.<TbMiddlePicPojo>lambdaQuery().in(TbMiddlePicPojo::getPicId, ids));
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<Long> middleIds = list.parallelStream().map(TbMiddlePicPojo::getMiddleId).toList();
        Set<Byte> types = list.parallelStream().map(TbMiddlePicPojo::getType).collect(Collectors.toSet());
        this.removePicMiddleIds(middleIds, types);
        tbPicService.removeBatchByIds(ids);
    }
    
    /**
     * @param middleIds 封面数据
     * @param types     封面类型
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    // TODO 需要修改pic type
    public void removePicMiddleIds(List<Long> middleIds, Collection<Byte> types) {
        this.removePicMiddleFile(middleIds, types, ossService::delete);
    }
    
    /**
     * 批量删除封面文件
     *
     * @param middleIds 封面的关联ID
     * @param types     封面类型
     * @param consumer  删除文件
     */
    @Transactional(rollbackFor = Exception.class)
    public void removePicMiddleFile(Collection<Long> middleIds, Collection<Byte> types, Consumer<List<String>> consumer) {
        if (CollUtil.isEmpty(middleIds)) {
            return;
        }
        synchronized (lock) {
            Wrapper<TbMiddlePicPojo> middlePicWrapper = Wrappers.<TbMiddlePicPojo>lambdaQuery()
                                                                .in(TbMiddlePicPojo::getMiddleId, middleIds)
                                                                .in(TbMiddlePicPojo::getType, types);
            List<TbMiddlePicPojo> middlePicList = middlePicService.list(middlePicWrapper);
            if (CollUtil.isNotEmpty(middlePicList)) {
                List<Long> picIds = middlePicList.parallelStream().map(TbMiddlePicPojo::getPicId).toList();
                List<TbPicPojo> picPojoList = tbPicService.listByIds(picIds);
                List<TbPicPojo> removePicIds = new ArrayList<>();
                List<TbPicPojo> updatePicPojoList = new ArrayList<>();
                for (TbPicPojo tbPicPojo : picPojoList) {
                    tbPicPojo.setCount(tbPicPojo.getCount() - 1);
                    if (tbPicPojo.getCount() > 0) {
                        updatePicPojoList.add(tbPicPojo);
                    } else if (tbPicPojo.getCount() == 0) {
                        removePicIds.add(tbPicPojo);
                        // 需要删除的封面不需要更新
                        updatePicPojoList.remove(tbPicPojo);
                    }
                }
                middlePicService.removeBatchByIds(middlePicList);
                if (CollUtil.isNotEmpty(updatePicPojoList)) {
                    tbPicService.updateBatchById(updatePicPojoList);
                }
                // 删除所有没有引用封面
                if (CollectionUtils.isNotEmpty(removePicIds)) {
                    tbPicService.removeByIds(removePicIds);
                    consumer.accept(removePicIds.parallelStream().map(TbPicPojo::getPath).toList());
                }
            }
        }
    }
    
    
    /**
     * 保存或更新封面
     *
     * @param id   添加封面关联ID,
     * @param type 添加ID类型 歌曲，专辑，歌单，歌手
     * @param file 封面数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdatePicFile(Long id, Byte type, File file) {
        String md5Hex;
        String upload;
        File dest = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            md5Hex = DigestUtil.md5Hex(file);
            dest = new File(md5Hex + StrPool.DOT + FileTypeUtil.getType(fis));
            File rename = FileUtil.copy(file, dest, true);
            upload = remoteStorageService.uploadPicFile(rename, md5Hex);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BaseException(ResultCode.IMG_DOWNLOAD_ERROR);
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        } finally {
            FileUtil.del(dest);
        }
        // TODO: 自动删除缓存文件 file
        TbPicPojo pojo = new TbPicPojo();
        pojo.setMd5(md5Hex);
        pojo.setPath(upload);
        this.saveOrUpdatePic(id, type, pojo);
    }
}
