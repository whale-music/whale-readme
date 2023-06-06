package org.api.subsonic.service;


import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.common.service.QukuAPI;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.core.common.constant.defaultinfo.DefaultInfo;
import org.core.pojo.TbMusicUrlPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(SubsonicConfig.SUBSONIC + "MediaRetrievalApi")
@Slf4j
public class MediaRetrievalApi {
    
    @Autowired
    private QukuAPI qukuService;
    
    @Autowired
    private DefaultInfo defaultInfo;
    
    public String getCoverArt(SubsonicCommonReq req, Long id) {
        log.debug(req.toString());
        String picUrl = qukuService.getPicUrl(id);
        if (StringUtils.isNotBlank(picUrl)) {
            return picUrl;
        }
        return defaultInfo.getPic().getDefaultPic();
    }
    
    public String stream(SubsonicCommonReq req, Long id) {
        log.debug(req.toString());
        List<TbMusicUrlPojo> musicUrlByMusicId = qukuService.getMusicUrlByMusicId(id, false);
        return CollUtil.isEmpty(musicUrlByMusicId) ? "" : musicUrlByMusicId.get(0).getUrl();
    }
}
