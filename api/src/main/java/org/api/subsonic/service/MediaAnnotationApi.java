package org.api.subsonic.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.star.StarRes;
import org.api.subsonic.model.res.unstar.UnStarRes;
import org.api.subsonic.utils.SubsonicUserUtil;
import org.core.common.constant.HistoryConstant;
import org.core.mybatis.iservice.*;
import org.core.mybatis.pojo.*;
import org.core.service.AccountService;
import org.core.service.QukuService;
import org.core.utils.CollectUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service(SubsonicConfig.SUBSONIC + "MediaAnnotationApi")
public class MediaAnnotationApi {
    
    private final QukuService qukuService;
    
    private final TbHistoryService tbHistoryService;
    
    private final TbMusicService tbMusicService;
    
    private final TbAlbumService albumService;
    
    private final TbArtistService tbArtistService;
    
    private final AccountService accountService;
    
    private final TbCollectService tbCollectService;
    
    private final TbMvService tbMvService;
    
    
    public MediaAnnotationApi(QukuService qukuService, TbHistoryService tbHistoryService, TbMusicService tbMusicService, TbAlbumService albumService, TbArtistService tbArtistService, AccountService accountService, TbCollectService tbCollectService, TbMvService tbMvService) {
        this.qukuService = qukuService;
        this.tbHistoryService = tbHistoryService;
        this.tbMusicService = tbMusicService;
        this.albumService = albumService;
        this.tbArtistService = tbArtistService;
        this.accountService = accountService;
        this.tbCollectService = tbCollectService;
        this.tbMvService = tbMvService;
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
    
    
    public void scrobble(SubsonicCommonReq req, Long id, Long timeStamp, Boolean submission) {
        String userName = req.getU();
        SysUserPojo userByName = accountService.getUserOrSubAccount(userName);
        if (updateHistory(tbMusicService.count(Wrappers.<TbMusicPojo>lambdaQuery().eq(TbMusicPojo::getId, id)),
                userByName,
                id,
                HistoryConstant.MUSIC,
                timeStamp)) {
            return;
        }
        
        // 专辑
        if (updateHistory(albumService.count(Wrappers.<TbAlbumPojo>lambdaQuery().eq(TbAlbumPojo::getId, id)),
                userByName,
                id,
                HistoryConstant.ALBUM,
                timeStamp)) {
            return;
        }
        
        // 歌手
        if (updateHistory(tbArtistService.count(Wrappers.<TbArtistPojo>lambdaQuery().eq(TbArtistPojo::getId, id)),
                userByName,
                id,
                HistoryConstant.ARTIST,
                timeStamp)) {
            return;
        }
        
        // 歌单
        if (updateHistory(tbCollectService.count(Wrappers.<TbCollectPojo>lambdaQuery().eq(TbCollectPojo::getId, id)),
                userByName,
                id,
                HistoryConstant.PLAYLIST,
                timeStamp)) {
            return;
        }
        
        // MV
        updateHistory(tbMvService.count(Wrappers.<TbMvPojo>lambdaQuery().eq(TbMvPojo::getId, id)), userByName, id, HistoryConstant.MV, timeStamp);
    }
    
    
    private boolean updateHistory(long count, SysUserPojo userByName, Long id, Byte type, Long playTime) {
        // 音乐
        if (count > 0) {
            TbHistoryPojo historyPojo = tbHistoryService.getOne(Wrappers.<TbHistoryPojo>lambdaQuery()
                                                                        .eq(TbHistoryPojo::getUserId, userByName.getId())
                                                                        .eq(TbHistoryPojo::getMiddleId, id)
                                                                        .eq(TbHistoryPojo::getType, type));
            if (Objects.isNull(historyPojo)) {
                TbHistoryPojo entity = new TbHistoryPojo();
                entity.setCount(1);
                entity.setMiddleId(id);
                entity.setUserId(userByName.getId());
                entity.setPlayedTime(playTime);
                entity.setType(type);
                tbHistoryService.save(entity);
            } else {
                historyPojo.setCount(historyPojo.getCount() + 1);
                tbHistoryService.updateById(historyPojo);
            }
            return true;
        }
        return false;
    }
}
