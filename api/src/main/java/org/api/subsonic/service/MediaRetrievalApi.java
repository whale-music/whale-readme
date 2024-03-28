package org.api.subsonic.service;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.common.service.QukuAPI;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.utils.spring.SubsonicResourceReturnStrategyUtil;
import org.core.common.constant.defaultinfo.DefaultInfo;
import org.core.common.properties.DebugConfig;
import org.core.mybatis.iservice.TbMiddlePicService;
import org.core.mybatis.iservice.TbPicService;
import org.core.mybatis.pojo.TbMiddlePicPojo;
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
    
    private final TbMiddlePicService tbMiddlePicService;
    
    public MediaRetrievalApi(QukuAPI qukuService, DefaultInfo defaultInfo, SubsonicResourceReturnStrategyUtil subsonicResourceReturnStrategyUtil, RemoteStorePicService remoteStorePicService, TbPicService tbPicService, TbMiddlePicService tbMiddlePicService) {
        this.qukuService = qukuService;
        this.defaultInfo = defaultInfo;
        this.subsonicResourceReturnStrategyUtil = subsonicResourceReturnStrategyUtil;
        this.remoteStorePicService = remoteStorePicService;
        this.tbMiddlePicService = tbMiddlePicService;
    }
    
    public String getCoverArt(SubsonicCommonReq req, String id, Long size) {
        Optional.ofNullable(DebugConfig.getDebugOption()).ifPresent(o -> log.debug("cover id: {}", id));
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
        // 查询数据库中的图片, 后续优化。使用字符串类型，同时包括数据封面ID和封面类型
        TbMiddlePicPojo one = tbMiddlePicService.getOne(Wrappers.<TbMiddlePicPojo>lambdaQuery().eq(TbMiddlePicPojo::getMiddleId, middlePic), false);
        String picUrl = remoteStorePicService.getPicUrl(one.getMiddleId(), one.getType());
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
