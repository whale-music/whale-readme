package org.api.common.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.core.common.constant.PicTypeConstant;
import org.core.common.constant.defaultinfo.DefaultInfo;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.HttpRequestConfig;
import org.core.model.MiddleTypeModel;
import org.core.mybatis.iservice.*;
import org.core.mybatis.pojo.TbMiddlePicPojo;
import org.core.mybatis.pojo.TbPicPojo;
import org.core.mybatis.pojo.TbResourcePojo;
import org.core.oss.service.OSSService;
import org.core.service.AccountService;
import org.core.service.RemoteStorageService;
import org.core.service.impl.QukuServiceImpl;
import org.core.utils.ImageTypeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service("qukuAPI")
@Slf4j
public class QukuAPI extends QukuServiceImpl {
    
    private final HttpRequestConfig httpRequestConfig;
    
    private final TbMiddlePicService tbMiddlePicService;
    
    private final OSSService ossService;
    
    private final RemoteStorageService remoteStorageService;
    
    public QukuAPI(TbMusicService musicService, TbAlbumService albumService, TbArtistService artistService, TbResourceService musicUrlService, TbUserAlbumService userAlbumService, TbAlbumArtistService albumArtistService, TbMusicArtistService musicArtistService, TbUserArtistService userSingerService, TbCollectMusicService collectMusicService, TbCollectService collectService, TbUserCollectService userCollectService, TbMiddleTagService middleTagService, TbLyricService lyricService, TbTagService tagService, AccountService accountService, TbPicService picService, TbMiddlePicService middlePicService, Cache<Long, TbPicPojo> picCache, Cache<MiddleTypeModel, Long> picMiddleCache, DefaultInfo defaultInfo, HttpRequestConfig httpRequestConfig, TbMvArtistService tbMvArtistService, TbOriginService tbOriginService, TbMiddlePicService tbMiddlePicService, OSSService ossService, RemoteStorageService remoteStorageService) {
        super(musicService,
                albumService,
                artistService,
                musicUrlService,
                userAlbumService,
                albumArtistService,
                musicArtistService,
                userSingerService,
                collectMusicService,
                collectService,
                userCollectService,
                middleTagService,
                lyricService,
                tagService,
                accountService,
                picService,
                middlePicService,
                picCache,
                picMiddleCache,
                defaultInfo,
                tbMvArtistService,
                tbOriginService
        );
        this.httpRequestConfig = httpRequestConfig;
        this.tbMiddlePicService = tbMiddlePicService;
        this.ossService = ossService;
        this.remoteStorageService = remoteStorageService;
    }
    
    /**
     * 封面
     *
     * @param middleIds 封面ID
     * @param type      关联ID类型
     * @return 封面地址
     */
    @Override
    public Map<Long, String> getPicUrl(Collection<Long> middleIds, Byte type) {
        Map<Long, String> picUrl = super.getPicPath(middleIds, type);
        return getPicUrlList(picUrl, false);
    }
    
    
    /**
     * 保存或更新封面
     *
     * @param id   添加封面关联ID,
     * @param type 添加ID类型 歌曲，专辑，歌单，歌手
     * @param file 封面数据
     */
    // @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdatePicFile(Long id, Byte type, File file) {
        String md5Hex;
        String upload;
        File dest = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            md5Hex = DigestUtil.md5Hex(file);
            dest = new File(md5Hex + ImageTypeUtils.getPicType(fis));
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
        // TODO: 自动删除缓存文件
        TbPicPojo pojo = new TbPicPojo();
        pojo.setMd5(md5Hex);
        pojo.setPath(upload);
        super.saveOrUpdatePic(id, type, pojo);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateAlbumPicFile(Long id, File file) {
        this.saveOrUpdatePicFile(id, PicTypeConstant.ALBUM, file);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateArtistPicFile(Long id, File file) {
        this.saveOrUpdatePicFile(id, PicTypeConstant.ARTIST, file);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateMusicPicFile(Long id, File file) {
        this.saveOrUpdatePicFile(id, PicTypeConstant.MUSIC, file);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateAvatarPicFile(Long id, File file) {
        this.saveOrUpdatePicFile(id, PicTypeConstant.USER_AVATAR, file);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateBackgroundPicFile(Long id, File file) {
        this.saveOrUpdatePicFile(id, PicTypeConstant.USER_BACKGROUND, file);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateMvPicFile(Long id, File file) {
        this.saveOrUpdatePicFile(id, PicTypeConstant.MV, file);
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
        super.saveOrUpdatePic(id, type, pojo);
    }
    
    /**
     * 批量删除封面文件
     *
     * @param ids      封面
     * @param consumer 删除文件
     */
    @Override
    protected void removePicFile(Collection<Long> ids, Collection<Byte> types, Consumer<List<String>> consumer) {
        // 删除歌曲文件
        super.removePicFile(ids, types, ossService::delete);
    }
    
    public List<TbResourcePojo> getMusicUrlByMusicId(Long musicId, boolean refresh) {
        return getMusicUrlByMusicId(CollUtil.newHashSet(musicId), refresh);
    }
    
    public List<TbResourcePojo> getMusicUrlByMusicId(Collection<Long> musicIds, boolean refresh) {
        if (CollUtil.isEmpty(musicIds)) {
            return Collections.emptyList();
        }
        return getMusicUrlByMusicUrlList(getMusicPaths(musicIds), refresh);
    }
    
    public TbResourcePojo getMusicUrlByMusicUrlList(TbResourcePojo musicUrlPojo, boolean refresh) {
        List<TbResourcePojo> urlList = getMusicUrlByMusicUrlList(Collections.singletonList(musicUrlPojo), refresh);
        if (CollUtil.isNotEmpty(urlList) && urlList.get(0) != null) {
            return urlList.get(0);
        }
        return new TbResourcePojo();
    }
    
    public List<TbResourcePojo> getMusicUrlByMusicUrlList(List<TbResourcePojo> list, boolean refresh) {
        for (TbResourcePojo tbMusicUrlPojo : list) {
            String s = remoteStorageService.getAddresses(tbMusicUrlPojo.getPath(), refresh);
            tbMusicUrlPojo.setPath(s);
        }
        return list;
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
     * 获取封面ID
     *
     * @param middleId 中间ID
     * @param type     封面类型
     * @return 封面ID
     */
    public Map<Long, Long> getPicIds(List<Long> middleId, Byte type) {
        List<TbMiddlePicPojo> list = tbMiddlePicService.list(Wrappers.<TbMiddlePicPojo>lambdaQuery()
                                                                     .in(TbMiddlePicPojo::getMiddleId, middleId)
                                                                     .eq(Objects.nonNull(type), TbMiddlePicPojo::getType, type));
        return list.parallelStream().collect(Collectors.toMap(TbMiddlePicPojo::getMiddleId, TbMiddlePicPojo::getPicId));
    }
    
    public Map<Long, Long> getMusicPicIds(List<Long> middleId) {
        return getPicIds(middleId, PicTypeConstant.MUSIC);
    }
    
    public Map<Long, Long> getArtistPicIds(List<Long> middleId) {
        return getPicIds(middleId, PicTypeConstant.ARTIST);
    }
    
    public Map<Long, Long> getAlbumPicIds(List<Long> middleId) {
        return getPicIds(middleId, PicTypeConstant.ALBUM);
    }
    
}
