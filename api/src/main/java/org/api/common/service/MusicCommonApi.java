package org.api.common.service;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.SaveConfig;
import org.core.pojo.TbMusicUrlPojo;
import org.core.service.QukuService;
import org.oss.factory.OSSFactory;
import org.oss.service.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
    
    public List<TbMusicUrlPojo> getMusicUrlByMusicId(Long musicId) {
        return getMusicUrlByMusicId(Set.of(musicId));
    }
    
    private static String getMusicAddresses(TbMusicUrlPojo tbMusicUrlPojo, SaveConfig config) {
        OSSService aList = OSSFactory.ossFactory("AList");
        boolean connected = aList.isConnected(config.getHost(), config.getAccessKey(), config.getSecretKey());
        if (!connected) {
            throw new BaseException(ResultCode.OSS_LOGIN_ERROR);
        }
        // 获取音乐地址
        return aList.getMusicAddresses(config.getHost(),
                config.getObjectSave(),
                tbMusicUrlPojo.getMd5() + "." + tbMusicUrlPojo.getEncodeType());
    }
    
    public List<TbMusicUrlPojo> getMusicUrlByMusicId(Set<Long> musicIds) {
        List<TbMusicUrlPojo> list = qukuService.getMusicUrl(musicIds);
        return getMusicUrlByMusicUrlList(list);
    }
    
    public TbMusicUrlPojo getMusicUrlByMusicUrlList(TbMusicUrlPojo musicUrlPojo) {
        List<TbMusicUrlPojo> urlList = getMusicUrlByMusicUrlList(Collections.singletonList(musicUrlPojo));
        if (CollUtil.isNotEmpty(urlList) && urlList.get(0) != null) {
            return urlList.get(0);
        }
        return new TbMusicUrlPojo();
    }
    
    public List<TbMusicUrlPojo> getMusicUrlByMusicUrlList(List<TbMusicUrlPojo> list) {
        for (TbMusicUrlPojo tbMusicUrlPojo : list) {
            try {
                String musicAddresses = getMusicAddresses(tbMusicUrlPojo, config);
                tbMusicUrlPojo.setUrl(musicAddresses);
            } catch (Exception e) {
                tbMusicUrlPojo.setUrl("");
                log.error("获取下载地址出错" + e.getMessage(), e);
            }
        }
        return list;
    }
}
