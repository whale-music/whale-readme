package org.api.subsonic.service;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.model.res.scanstatus.ScanStatusRes;
import org.api.subsonic.model.res.startscan.StartScanRes;
import org.core.mybatis.iservice.TbMusicService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

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
        String format = DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSX");
        res.setScanStatus(new ScanStatusRes.ScanStatus(format,false, count, 1));
        return res;
    }
    @Cacheable(value = "ScanStatus", key = "#root.methodName")
    public StartScanRes startScan(SubsonicCommonReq req) {
        StartScanRes res = new StartScanRes();
        long count = tbMusicService.count();
        String format = DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSX");
        res.setScanStatus(new StartScanRes.ScanStatus(format, false, count, 1));
        return res;
    }
}
