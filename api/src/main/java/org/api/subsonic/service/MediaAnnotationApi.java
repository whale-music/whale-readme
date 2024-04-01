package org.api.subsonic.service;

import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.star.StarRes;
import org.api.subsonic.model.res.unstar.UnStarRes;
import org.api.subsonic.utils.SubsonicUserUtil;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.service.QukuService;
import org.core.utils.CollectUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service(SubsonicConfig.SUBSONIC + "MediaAnnotationApi")
public class MediaAnnotationApi {
    
    private final QukuService qukuService;
    
    public MediaAnnotationApi(QukuService qukuService) {
        this.qukuService = qukuService;
    }
    
    public StarRes star(SubsonicCommonReq req, List<Long> id, List<Long> albumId, List<Long> artistId) {
        SysUserPojo user = SubsonicUserUtil.getUser();
        // 音乐
        if (CollectUtil.isNotEmpty(id)) {
            qukuService.collectLike(user.getId(), id, true);
        } else if (CollectUtil.isNotEmpty(albumId)) {
            qukuService.likeAlbum(user.getId(), albumId);
        } else if (CollectUtil.isNotEmpty(artistId)) {
            qukuService.likeArtist(user.getId(), artistId);
        }
        return new StarRes();
    }
    
    public UnStarRes unStar(SubsonicCommonReq req, List<Long> ids, List<Long> albumIds, List<Long> artistIds) {
        SysUserPojo user = SubsonicUserUtil.getUser();
        // 音乐
        if (CollectUtil.isNotEmpty(ids)) {
            qukuService.collectLike(user.getId(), ids, false);
        } else if (CollectUtil.isNotEmpty(albumIds)) {
            qukuService.unLikeAlbum(user.getId(), albumIds);
        } else if (CollectUtil.isNotEmpty(artistIds)) {
            qukuService.unLikeArtist(user.getId(), artistIds);
        }
        return new UnStarRes();
    }
}
