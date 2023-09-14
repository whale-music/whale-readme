package org.api.subsonic.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.common.service.QukuAPI;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.utils.spring.SubsonicResourceReturnStrategyUtil;
import org.core.common.constant.defaultinfo.DefaultInfo;
import org.core.mybatis.pojo.TbResourcePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service(SubsonicConfig.SUBSONIC + "MediaRetrievalApi")
@Slf4j
public class MediaRetrievalApi {
    
    @Autowired
    private QukuAPI qukuService;
    
    @Autowired
    private DefaultInfo defaultInfo;
    
    @Autowired
    private SubsonicResourceReturnStrategyUtil subsonicResourceReturnStrategyUtil;
    
    public String getCoverArt(SubsonicCommonReq req, Long id, Long size) {
        log.debug(req.toString());
        String picUrl = qukuService.getPicUrl(id, null);
        if (StringUtils.isNotBlank(picUrl)) {
            return picUrl;
        }
        return defaultInfo.getPic().getDefaultPic();
    }
    
    public String stream(SubsonicCommonReq req, Long id, String maxBitRate, String format, String timeOffset, String size, String estimateContentLength, String converted) {
        List<TbResourcePojo> musicUrlByMusicId = qukuService.getMusicUrlByMusicId(id, false);
        TbResourcePojo tbResourcePojo = subsonicResourceReturnStrategyUtil.handleResource(musicUrlByMusicId);
        return Optional.ofNullable(tbResourcePojo).orElse(new TbResourcePojo()).getPath();
    }
}
