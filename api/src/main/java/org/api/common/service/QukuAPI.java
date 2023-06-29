package org.api.common.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.core.common.constant.defaultinfo.DefaultInfo;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.HttpRequestConfig;
import org.core.config.SaveConfig;
import org.core.mybatis.iservice.*;
import org.core.mybatis.pojo.TbPicPojo;
import org.core.mybatis.pojo.TbResourcePojo;
import org.core.service.AccountService;
import org.core.service.impl.QukuServiceImpl;
import org.core.utils.ImageTypeUtils;
import org.oss.factory.OSSFactory;
import org.oss.service.OSSService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

@Service("QukuAPI")
@Slf4j
public class QukuAPI extends QukuServiceImpl {
    
    private final SaveConfig config;
    
    private final HttpRequestConfig httpRequestConfig;
    
    public QukuAPI(TbMusicService musicService, TbAlbumService albumService, TbArtistService artistService, TbResourceService musicUrlService, TbUserAlbumService userAlbumService, TbAlbumArtistService albumArtistService, TbMusicArtistService musicArtistService, TbUserArtistService userSingerService, TbCollectMusicService collectMusicService, TbCollectService collectService, TbUserCollectService userCollectService, TbMiddleTagService middleTagService, TbLyricService lyricService, TbTagService tagService, AccountService accountService, TbPicService picService, TbMiddlePicService middlePicService, Cache<Long, TbPicPojo> picCache, Cache<Long, Long> picMiddleCache, DefaultInfo defaultInfo, SaveConfig config, HttpRequestConfig httpRequestConfig) {
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
                defaultInfo);
        this.config = config;
        this.httpRequestConfig = httpRequestConfig;
    }
    
    
    private static String getAddresses(String url, SaveConfig config, boolean refresh) {
        // 获取音乐地址
        return OSSFactory.ossFactory(config).getAddresses(url, refresh);
    }
    
    /**
     * 封面
     *
     * @param ids  封面ID
     * @param type 关联ID类型
     * @return 封面地址
     */
    @Override
    public Map<Long, String> getPicUrl(Collection<Long> ids, Byte type) {
        Map<Long, String> picUrl = super.getPicUrl(ids, type);
        return getPicUrlList(picUrl, false);
    }
    
    /**
     * 保存或更新封面
     *
     * @param id   添加封面关联ID,
     * @param type 添加ID类型 歌曲，专辑，歌单，歌手
     * @param pojo 封面数据
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdatePic(Long id, Byte type, TbPicPojo pojo) {
        if (StringUtils.isBlank(pojo.getUrl())) {
            throw new BaseException(ResultCode.PARAM_IS_INVALID);
        }
        OSSService ossService = OSSFactory.ossFactory(config);
        // 下载封面, 保存文件名为md5
        String randomName = System.currentTimeMillis() + "" + RandomUtils.nextLong();
        String dirPath = httpRequestConfig.getTempPath() + FileUtil.FILE_SEPARATOR + randomName;
        String md5Hex;
        String upload;
        File rename = null;
        File fileFromUrl = HttpUtil.downloadFileFromUrl(pojo.getUrl(), FileUtil.touch(dirPath), httpRequestConfig.getTimeout());
        try (FileInputStream fis = new FileInputStream(fileFromUrl)) {
            md5Hex = DigestUtil.md5Hex(fileFromUrl);
            rename = FileUtil.rename(fileFromUrl, md5Hex + ImageTypeUtils.getPicType(fis), false, true);
            // 删除文件
            upload = ossService.upload(config.getImgSave(), config.getAssignImgSave(), rename, md5Hex);
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
        pojo.setMd5(md5Hex);
        pojo.setUrl(upload);
        super.saveOrUpdatePic(id, type, pojo);
    }
    
    /**
     * 批量删除封面文件
     *
     * @param ids      封面
     * @param consumer 删除文件
     */
    @Override
    protected void removePicFile(Collection<Long> ids, Consumer<List<String>> consumer) {
        // 删除歌曲文件
        OSSService ossService = OSSFactory.ossFactory(config);
        super.removePicFile(ids, ossService::delete);
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
            String s = getAddresses(refresh, tbMusicUrlPojo.getPath());
            tbMusicUrlPojo.setPath(s);
        }
        return list;
    }
    
    private String getAddresses(boolean refresh, String path) {
        try {
            return getAddresses(path, config, refresh);
        } catch (BaseException e) {
            if (Objects.equals(e.getErrorCode(), ResultCode.SONG_NOT_EXIST.getCode())) {
                log.warn("获取下载地址出错: {}", e.getMessage());
                return "";
            }
            log.error(e.getMessage(), e);
            throw new BaseException(e.getErrorCode(), e.getErrorMsg());
        }
    }
    
    public String getPicUrl(String path, boolean refresh) {
        return StringUtils.startsWithIgnoreCase("http", path) ? path : getAddresses(refresh, path);
    }
    
    public List<TbPicPojo> getPicList(List<TbPicPojo> picPojoList, boolean refresh) {
        for (TbPicPojo tbPicPojo : picPojoList) {
            tbPicPojo.setUrl(getAddresses(refresh, tbPicPojo.getUrl()));
        }
        return picPojoList;
    }
    
    public Map<Long, String> getPicUrlList(Map<Long, String> paths, boolean refresh) {
        for (Map.Entry<Long, String> longStringEntry : paths.entrySet()) {
            String s;
            if (StringUtils.startsWithIgnoreCase("http", longStringEntry.getValue())) {
                s = longStringEntry.getValue();
            } else {
                s = getAddresses(refresh, longStringEntry.getValue());
            }
            paths.put(longStringEntry.getKey(), s);
        }
        return paths;
    }
    
    
    public Collection<String> getMD5(boolean refresh) {
        return OSSFactory.ossFactory(config).getAllMD5(refresh);
    }
    
    public Collection<String> getMD5(String md5, boolean refresh) {
        return OSSFactory.ossFactory(config).getAllMD5(md5, refresh);
    }
    
}
