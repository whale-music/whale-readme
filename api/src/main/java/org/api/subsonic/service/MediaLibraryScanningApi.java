package org.api.subsonic.service;

import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.model.res.scanstatus.ScanStatusRes;
import org.core.mybatis.iservice.TbMusicService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class MediaLibraryScanningApi {
    private final TbMusicService tbMusicService;
    
    public MediaLibraryScanningApi(TbMusicService tbMusicService) {
        this.tbMusicService = tbMusicService;
    }
    
    @Cacheable(value = "ScanStatus", key = "#root.methodName")
    public ScanStatusRes getScanStatus(SubsonicCommonReq req) {
        ScanStatusRes res = new ScanStatusRes();
        long count = tbMusicService.count();
        res.setScanStatus(new ScanStatusRes.ScanStatus(true, count));
        return res;
    }
}
