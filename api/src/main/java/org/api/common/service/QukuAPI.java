package org.api.common.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.core.common.constant.PicTypeConstant;
import org.core.common.constant.defaultinfo.DefaultInfo;
import org.core.mybatis.iservice.*;
import org.core.mybatis.pojo.TbMiddlePicPojo;
import org.core.mybatis.pojo.TbResourcePojo;
import org.core.service.AccountService;
import org.core.service.RemoteStorageService;
import org.core.service.RemoteStorePicService;
import org.core.service.impl.QukuServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("qukuAPI")
@Slf4j
public class QukuAPI extends QukuServiceImpl {
    
    private final TbMiddlePicService tbMiddlePicService;
    
    private final RemoteStorageService remoteStorageService;
    
    
    public QukuAPI(TbMusicService musicService, TbAlbumService albumService, TbArtistService artistService, TbResourceService musicUrlService, TbUserAlbumService userAlbumService, TbAlbumArtistService albumArtistService, TbMusicArtistService musicArtistService, TbUserArtistService userSingerService, TbCollectMusicService collectMusicService, TbCollectService collectService, TbUserCollectService userCollectService, TbMiddleTagService middleTagService, TbLyricService lyricService, TbTagService tagService, AccountService accountService, DefaultInfo defaultInfo, TbMvArtistService tbMvArtistService, TbOriginService tbOriginService, TbMiddlePicService tbMiddlePicService, RemoteStorePicService remoteStorePicService, RemoteStorageService remoteStorageService) {
        super(musicService,
                albumService,
                artistService,
                musicUrlService,
                userAlbumService,
                albumArtistService,
                musicArtistService,
                userSingerService,
                collectMusicService,
                collectService,
                userCollectService,
                middleTagService,
                lyricService,
                tagService,
                accountService,
                defaultInfo,
                tbMvArtistService,
                tbOriginService,
                remoteStorePicService
        );
        this.tbMiddlePicService = tbMiddlePicService;
        this.remoteStorageService = remoteStorageService;
    }
    
    public List<TbResourcePojo> getMusicUrlByMusicId(Long musicId, boolean refresh) {
        return getMusicUrlByMusicId(CollUtil.newHashSet(musicId), refresh);
    }
    
    public List<TbResourcePojo> getMusicUrlByMusicId(Collection<Long> musicIds, boolean refresh) {
        if (CollUtil.isEmpty(musicIds)) {
            return Collections.emptyList();
        }
        return getMusicUrlByMusicUrlList(getMusicPaths(musicIds), refresh);
    }
    
    public TbResourcePojo getMusicUrlByMusicUrlList(TbResourcePojo musicUrlPojo, boolean refresh) {
        List<TbResourcePojo> urlList = getMusicUrlByMusicUrlList(Collections.singletonList(musicUrlPojo), refresh);
        if (CollUtil.isNotEmpty(urlList) && urlList.get(0) != null) {
            return urlList.get(0);
        }
        return new TbResourcePojo();
    }
    
    public List<TbResourcePojo> getMusicUrlByMusicUrlList(List<TbResourcePojo> list, boolean refresh) {
        for (TbResourcePojo tbMusicUrlPojo : list) {
            String s = remoteStorageService.getAddresses(tbMusicUrlPojo.getPath(), refresh);
            tbMusicUrlPojo.setPath(s);
        }
        return list;
    }
    
    /**
     * 获取封面ID
     *
     * @param middleId 中间ID
     * @param type     封面类型
     * @return 封面ID
     */
    public Map<Long, Long> getPicIds(List<Long> middleId, Byte type) {
        List<TbMiddlePicPojo> list = tbMiddlePicService.list(Wrappers.<TbMiddlePicPojo>lambdaQuery()
                                                                     .in(TbMiddlePicPojo::getMiddleId, middleId)
                                                                     .eq(Objects.nonNull(type), TbMiddlePicPojo::getType, type));
        return list.parallelStream().collect(Collectors.toMap(TbMiddlePicPojo::getMiddleId, TbMiddlePicPojo::getPicId));
    }
    
    public Map<Long, Long> getMusicPicIds(List<Long> middleId) {
        return getPicIds(middleId, PicTypeConstant.MUSIC);
    }
    
    public Map<Long, Long> getArtistPicIds(List<Long> middleId) {
        return getPicIds(middleId, PicTypeConstant.ARTIST);
    }
    
    public Map<Long, Long> getAlbumPicIds(List<Long> middleId) {
        return getPicIds(middleId, PicTypeConstant.ALBUM);
    }
    
}
