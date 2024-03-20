package org.api.subsonic.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.common.service.QukuAPI;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.utils.spring.SubsonicResourceReturnStrategyUtil;
import org.core.common.constant.defaultinfo.DefaultInfo;
import org.core.mybatis.pojo.TbResourcePojo;
import org.core.service.RemoteStorePicService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service(SubsonicConfig.SUBSONIC + "MediaRetrievalApi")
@Slf4j
public class MediaRetrievalApi {
    
    private final QukuAPI qukuService;
    
    private final DefaultInfo defaultInfo;
    
    private final SubsonicResourceReturnStrategyUtil subsonicResourceReturnStrategyUtil;
    
    private final RemoteStorePicService remoteStorePicService;
    
    public MediaRetrievalApi(QukuAPI qukuService, DefaultInfo defaultInfo, SubsonicResourceReturnStrategyUtil subsonicResourceReturnStrategyUtil, RemoteStorePicService remoteStorePicService) {
        this.qukuService = qukuService;
        this.defaultInfo = defaultInfo;
        this.subsonicResourceReturnStrategyUtil = subsonicResourceReturnStrategyUtil;
        this.remoteStorePicService = remoteStorePicService;
    }
    
    public String getCoverArt(SubsonicCommonReq req, String id, Long size) {
        log.debug("cover id: {}", id);
        // 与封面关联的id，比如歌单，音乐，专辑，歌手
        long middlePic;
        // 处理一些前端会添加 歌单封面前缀 pl-
        String playPrefix = "pl-";
        String albumPrefix = "al-";
        // 可能是media file 媒体文件缩写
        String mfPrefix = "mf-";
        if (StringUtils.startsWithIgnoreCase(id, playPrefix)) {
            String playListPic = StringUtils.replace(id, playPrefix, "");
            middlePic = Long.parseLong(playListPic);
        } else if (StringUtils.startsWithIgnoreCase(id, albumPrefix)) {
            String playListPic = StringUtils.replace(id, albumPrefix, "");
            middlePic = Long.parseLong(playListPic);
        } else if (StringUtils.startsWithIgnoreCase(id, mfPrefix)) {
            String playListPic = StringUtils.replace(id, mfPrefix, "");
            middlePic = Long.parseLong(playListPic);
        } else {
            try {
                middlePic = Long.parseLong(id);
            } catch (NumberFormatException e) {
                return defaultInfo.getPic().getDefaultPic();
            }
        }
        String picUrl = remoteStorePicService.getPicUrl(middlePic, null);
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
