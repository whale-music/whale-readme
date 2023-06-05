package org.api.common.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.HttpRequestConfig;
import org.core.config.SaveConfig;
import org.core.pojo.TbMusicUrlPojo;
import org.core.pojo.TbPicPojo;
import org.core.service.impl.QukuServiceImpl;
import org.oss.factory.OSSFactory;
import org.oss.service.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

@Service("QukuAPI")
@Slf4j
public class QukuAPI extends QukuServiceImpl {
    
    @Autowired
    private SaveConfig config;
    
    @Autowired
    private HttpRequestConfig httpRequestConfig;
    
    private static String getAddresses(String url, SaveConfig config, boolean refresh) {
        // 获取音乐地址
        return OSSFactory.ossFactory(config).getAddresses(url, refresh);
    }
    
    /**
     * 封面
     *
     * @param id 封面ID
     * @return 封面地址
     */
    @Override
    public String getPicUrl(Long id) {
        String picUrl = super.getPicUrl(id);
        return getPicUrl(picUrl, false);
    }
    
    /**
     * 封面
     *
     * @param ids 封面ID
     * @return 封面地址
     */
    @Override
    public Map<Long, String> getPicUrl(Collection<Long> ids) {
        Map<Long, String> picUrl = super.getPicUrl(ids);
        return getPicUrlList(picUrl, false);
    }
    
    /**
     * 保存封面
     *
     * @param pic      封面
     * @param consumer 删除封面文件
     */
    @Override
    public TbPicPojo saveOrUpdatePic(TbPicPojo pic, Consumer<String> consumer) {
        if (StringUtils.isBlank(pic.getUrl())) {
            return new TbPicPojo();
        }
        OSSService ossService = OSSFactory.ossFactory(config);
        String[] split = pic.getUrl().split("\\.");
        String suffix = split[split.length - 1];
        String randomName = System.currentTimeMillis() + "" + RandomUtils.nextLong();
        String dirPath = httpRequestConfig.getTempPath() + FileUtil.FILE_SEPARATOR + randomName + "." + suffix;
        File fileFromUrl = HttpUtil.downloadFileFromUrl(pic.getUrl(), FileUtil.touch(dirPath), httpRequestConfig.getTimeout());
        String md5Hex = DigestUtil.md5Hex(fileFromUrl);
        File rename = FileUtil.rename(fileFromUrl, md5Hex, true, true);
        String upload = ossService.upload(config.getImgSave(), config.getAssignImgSave(), rename, md5Hex);
        FileUtil.del(fileFromUrl);
        pic.setMd5(md5Hex);
        pic.setUrl(upload);
        return super.saveOrUpdatePic(pic, ossService::delete);
    }
    
    public TbPicPojo saveOrUpdatePic(TbPicPojo pic) {
        return this.saveOrUpdatePic(pic, null);
    }
    
    public List<TbMusicUrlPojo> getMusicUrlByMusicId(Long musicId, boolean refresh) {
        return getMusicUrlByMusicId(CollUtil.newHashSet(musicId), refresh);
    }
    
    public List<TbMusicUrlPojo> getMusicUrlByMusicId(Collection<Long> musicIds, boolean refresh) {
        if (CollUtil.isEmpty(musicIds)) {
            return Collections.emptyList();
        }
        return getMusicUrlByMusicUrlList(getMusicPaths(musicIds), refresh);
    }
    
    public TbMusicUrlPojo getMusicUrlByMusicUrlList(TbMusicUrlPojo musicUrlPojo, boolean refresh) {
        List<TbMusicUrlPojo> urlList = getMusicUrlByMusicUrlList(Collections.singletonList(musicUrlPojo), refresh);
        if (CollUtil.isNotEmpty(urlList) && urlList.get(0) != null) {
            return urlList.get(0);
        }
        return new TbMusicUrlPojo();
    }
    
    public List<TbMusicUrlPojo> getMusicUrlByMusicUrlList(List<TbMusicUrlPojo> list, boolean refresh) {
        for (TbMusicUrlPojo tbMusicUrlPojo : list) {
            String s = getAddresses(refresh, tbMusicUrlPojo.getUrl());
            tbMusicUrlPojo.setUrl(s);
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
            throw new BaseException(e.getErrorCode(), e.getErrorCode());
        }
    }
    
    public String getPicUrl(String path, boolean refresh) {
        return getAddresses(refresh, path);
    }
    
    public List<TbPicPojo> getPicList(List<TbPicPojo> picPojoList, boolean refresh) {
        for (TbPicPojo tbPicPojo : picPojoList) {
            tbPicPojo.setUrl(getAddresses(refresh, tbPicPojo.getUrl()));
        }
        return picPojoList;
    }
    
    public Map<Long, String> getPicUrlList(Map<Long, String> paths, boolean refresh) {
        for (Map.Entry<Long, String> longStringEntry : paths.entrySet()) {
            String s = getAddresses(refresh, longStringEntry.getValue());
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
