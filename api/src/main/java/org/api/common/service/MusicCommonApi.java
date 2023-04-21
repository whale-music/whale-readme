package org.api.common.service;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.SaveConfig;
import org.core.pojo.TbMusicUrlPojo;
import org.core.service.QukuService;
import org.oss.factory.OSSFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service("MusicCommonApi")
public class MusicCommonApi {
    
    
    @Autowired
    private QukuService qukuService;
    
    /**
     * 访问音乐文件地址
     */
    @Autowired
    private SaveConfig config;
    
    private static String getMusicAddresses(TbMusicUrlPojo tbMusicUrlPojo, SaveConfig config, boolean refresh) {
        // 获取音乐地址
        return OSSFactory.ossFactory(config).getMusicAddresses(tbMusicUrlPojo.getUrl(), refresh);
    }
    
    public List<TbMusicUrlPojo> getMusicUrlByMusicId(Long musicId, boolean refresh) {
        return getMusicUrlByMusicId(Set.of(musicId), refresh);
    }
    
    public List<TbMusicUrlPojo> getMusicUrlByMusicId(Set<Long> musicIds, boolean refresh) {
        if (CollUtil.isEmpty(musicIds)) {
            return Collections.emptyList();
        }
        List<TbMusicUrlPojo> list = qukuService.getMusicUrl(musicIds);
        return getMusicUrlByMusicUrlList(list, refresh);
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
            try {
                String musicAddresses = getMusicAddresses(tbMusicUrlPojo, config, refresh);
                tbMusicUrlPojo.setUrl(musicAddresses);
            } catch (BaseException e) {
                if (Objects.equals(e.getErrorCode(), ResultCode.SONG_NOT_EXIST.getCode())) {
                    tbMusicUrlPojo.setUrl("");
                    log.error("获取下载地址出错: {}", e.getMessage());
                    continue;
                }
                throw new BaseException(e.getErrorCode(), e.getErrorCode());
            }
        }
        return list;
    }
    
    public Collection<String> getMusicMD5(boolean refresh) {
        return OSSFactory.ossFactory(config).getMusicAllMD5(refresh);
    }
    
    public Collection<String> getMusicMD5(String md5, boolean refresh) {
        return OSSFactory.ossFactory(config).getMusicAllMD5(md5, refresh);
    }
}
