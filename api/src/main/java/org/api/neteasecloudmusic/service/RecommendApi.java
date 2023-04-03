package org.api.neteasecloudmusic.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.common.service.MusicCommonApi;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.personalfm.Album;
import org.api.neteasecloudmusic.model.vo.personalfm.ArtistsItem;
import org.api.neteasecloudmusic.model.vo.personalfm.DataItem;
import org.api.neteasecloudmusic.model.vo.personalfm.PersonalFMRes;
import org.api.neteasecloudmusic.model.vo.personalized.PersonalizedRes;
import org.api.neteasecloudmusic.model.vo.personalized.ResultItem;
import org.api.neteasecloudmusic.model.vo.recommend.albumnew.Artist;
import org.api.neteasecloudmusic.model.vo.recommend.albumnew.RecommendAlbumNewRes;
import org.api.neteasecloudmusic.model.vo.recommend.resource.Creator;
import org.api.neteasecloudmusic.model.vo.recommend.resource.DailyRecommendResourceRes;
import org.api.neteasecloudmusic.model.vo.recommend.songs.*;
import org.core.common.page.Page;
import org.core.pojo.*;
import org.core.service.AccountService;
import org.core.service.PlayListService;
import org.core.service.QukuService;
import org.core.utils.AliasUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "RecommendApi")
public class RecommendApi {
    
    /**
     * 曲库数据库操作层
     */
    @Autowired
    private QukuService qukuService;
    
    
    /**
     * 音乐调用外部API
     */
    @Autowired
    private MusicCommonApi musicCommonApi;
    
    
    @Autowired
    private PlayListService playListService;
    
    @Autowired
    private AccountService accountService;
    
    public PersonalFMRes personalFM() {
        PersonalFMRes res = new PersonalFMRes();
        res.setData(new ArrayList<>());
        for (int i = 0; i < 3; i++) {
            MusicPojo musicPojo = qukuService.randomMusic();
            // 无数据直接返回
            if (musicPojo == null) {
                return res;
            }
            // 填充数据
            DataItem e = new DataItem();
            // 歌曲信息
            e.setId(musicPojo.getId());
            e.setName(musicPojo.getMusicName());
            e.setAlias(List.of(musicPojo.getAliasName().split(",")));
            // 歌曲下载地址
            List<MusicUrlPojo> musicUrlByMusicId = musicCommonApi.getMusicUrlByMusicId(musicPojo.getId());
            if (CollUtil.isNotEmpty(musicUrlByMusicId)) {
                e.setMp3Url(musicUrlByMusicId.get(0).getUrl());
            }
            e.setDuration(musicPojo.getTimeLength());
            
            // 专辑信息
            AlbumPojo byId = qukuService.getAlbumByAlbumId(musicPojo.getAlbumId());
            Album album = new Album();
            album.setName(byId.getAlbumName());
            album.setId(byId.getId());
            album.setPicUrl(byId.getPic());
            e.setAlbum(album);
            
            // 歌手信息
            ArrayList<ArtistsItem> artists = new ArrayList<>();
            List<ArtistPojo> singerByMusicId = qukuService.getSingerByMusicId(musicPojo.getId());
            int albumSize = qukuService.getAlbumMusicCountByAlbumId(musicPojo.getAlbumId());
            for (ArtistPojo artistPojo : singerByMusicId) {
                ArtistsItem artistsItem = new ArtistsItem();
                artistsItem.setName(artistPojo.getArtistName());
                artistsItem.setAlbumSize(albumSize);
                artistsItem.setId(artistPojo.getId());
                artistsItem.setImg1v1Url(artistPojo.getPic());
                artistsItem.setPicUrl(artistPojo.getPic());
                artists.add(artistsItem);
            }
            e.setArtists(artists);
            res.getData().add(e);
        }
        
        return res;
    }
    
    
    public PersonalizedRes personalized(Long limit) {
        List<CollectPojo> collectPojos = playListService.randomPlayList(limit);
        PersonalizedRes personalizedRes = new PersonalizedRes();
        List<ResultItem> result = new ArrayList<>();
        for (CollectPojo collectPojo : collectPojos) {
            ResultItem e = new ResultItem();
            e.setId(collectPojo.getId());
            e.setName(collectPojo.getPlayListName());
            e.setPicUrl(collectPojo.getPic());
            e.setCanDislike(true);
            e.setTrackNumberUpdateTime(collectPojo.getUpdateTime().getNano());
            result.add(e);
        }
        personalizedRes.setResult(result);
        return personalizedRes;
    }
    
    public List<DailyRecommendResourceRes> recommendResource(int limit) {
        List<CollectPojo> collectPojos = playListService.randomPlayList((long) limit);
        List<DailyRecommendResourceRes> res = new ArrayList<>();
        for (CollectPojo collectPojo : collectPojos) {
            DailyRecommendResourceRes e = new DailyRecommendResourceRes();
            e.setId(collectPojo.getId());
            e.setName(collectPojo.getPlayListName());
            e.setPicUrl(collectPojo.getPic());
            e.setPlaycount(3000L);
            e.setCreateTime((long) collectPojo.getCreateTime().getNano());
            e.setUserId(collectPojo.getUserId());
            
            // 创建者信息
            SysUserPojo userPojo = accountService.getById(collectPojo.getUserId());
            Creator creator = new Creator();
            creator.setAvatarUrl(userPojo.getAvatarUrl());
            creator.setBackgroundUrl(userPojo.getBackgroundUrl());
            creator.setNickname(userPojo.getNickname());
            creator.setDescription(userPojo.getSignature());
            creator.setSignature(userPojo.getSignature());
            e.setCreator(creator);
            res.add(e);
        }
        return res;
    }
    
    public RecommendSongerRes recommendSongs(int limit) {
        RecommendSongerRes res = new RecommendSongerRes();
        List<DailySongsItem> dailySongsItems = new ArrayList<>();
        List<RecommendReasonsItem> recommendReasons = new ArrayList<>();
        
        String reason = "超42%人收藏";
        for (int i = 0; i < limit; i++) {
            MusicPojo musicPojo = qukuService.randomMusic();
            DailySongsItem dailySongsItem = new DailySongsItem();
            dailySongsItem.setName(musicPojo.getMusicName());
            dailySongsItem.setId(musicPojo.getId());
            dailySongsItem.setPublishTime(musicPojo.getCreateTime().getNano());
            dailySongsItem.setReason(reason);
            dailySongsItem.setRecommendReason(reason);
            
            // 歌手信息
            ArrayList<org.api.neteasecloudmusic.model.vo.recommend.songs.Artist> artists = new ArrayList<>();
            List<ArItem> ar = new ArrayList<>();
            List<ArtistPojo> singerByMusicId = qukuService.getSingerByMusicId(musicPojo.getId());
            for (ArtistPojo artistPojo : singerByMusicId) {
                ArItem arItem = new ArItem();
                arItem.setName(artistPojo.getArtistName());
                arItem.setId(artistPojo.getId());
                arItem.setAlias(AliasUtil.getAliasList(artistPojo.getAliasName()));
                ar.add(arItem);
                
                // 兼容web api
                org.api.neteasecloudmusic.model.vo.recommend.songs.Artist artist = new org.api.neteasecloudmusic.model.vo.recommend.songs.Artist();
                artist.setName(artistPojo.getArtistName());
                artist.setId(artistPojo.getId());
                artist.setPicUrl(artistPojo.getPic());
                artist.setAlias(AliasUtil.getAliasList(artistPojo.getAliasName()));
                artist.setMusicSize(qukuService.getMusicCountBySingerId(artistPojo.getId()));
                artist.setAlbumSize(qukuService.getAlbumCountBySingerId(musicPojo.getId()));
                artists.add(artist);
            }
            dailySongsItem.setAr(ar);
            dailySongsItem.setArtists(artists);
            
            
            AlbumPojo albumByAlbumId = qukuService.getAlbumByAlbumId(musicPojo.getAlbumId());
            Al al = new Al();
            al.setPicStr(albumByAlbumId.getPic());
            al.setId(albumByAlbumId.getId());
            al.setName(albumByAlbumId.getAlbumName());
            dailySongsItem.setAl(al);
            
            // 兼容web api
            org.api.neteasecloudmusic.model.vo.recommend.songs.Album album = new org.api.neteasecloudmusic.model.vo.recommend.songs.Album();
            album.setPicUrl(albumByAlbumId.getPic());
            album.setArtist(CollUtil.isEmpty(artists) ? null : artists.get(0));
            album.setId(albumByAlbumId.getId());
            album.setName(albumByAlbumId.getAlbumName());
            album.setCompany(albumByAlbumId.getCompany());
            album.setArtists(artists);
            album.setBlurPicUrl(albumByAlbumId.getPic());
            album.setSubType(albumByAlbumId.getSubType());
            album.setSize(qukuService.getAlbumMusicCountByAlbumId(albumByAlbumId.getId()));
            album.setPublishTime((long) albumByAlbumId.getPublishTime().getNano());
            dailySongsItem.setAlbum(album);
            
            Privilege privilege = new Privilege();
            privilege.setId(musicPojo.getId());
            dailySongsItem.setPrivilege(privilege);
            dailySongsItems.add(dailySongsItem);
            
            
            RecommendReasonsItem recommendReasonsItem = new RecommendReasonsItem();
            recommendReasonsItem.setSongId(musicPojo.getId());
            recommendReasonsItem.setReason(reason);
            recommendReasons.add(recommendReasonsItem);
        }
        
        res.setRecommendReasons(recommendReasons);
        res.setDailySongs(dailySongsItems);
        return res;
    }
    
    public Page<RecommendAlbumNewRes> albumNew(String area, Integer offset, Integer limit) {
        Page<AlbumPojo> albumPojoList = qukuService.getAlbumPage(area, offset, limit);
        Page<RecommendAlbumNewRes> recommendAlbumNewResPage = new Page<>();
        recommendAlbumNewResPage.setRecords(new ArrayList<>());
        for (AlbumPojo albumPojo : albumPojoList.getRecords()) {
            RecommendAlbumNewRes recommendAlbumNewRes = new RecommendAlbumNewRes();
            recommendAlbumNewRes.setId(albumPojo.getId());
            recommendAlbumNewRes.setSize(qukuService.getAlbumCountBySingerId(albumPojo.getId()));
            recommendAlbumNewRes.setPublishTime((long) albumPojo.getCreateTime().getNano());
            recommendAlbumNewRes.setName(albumPojo.getAlbumName());
            recommendAlbumNewRes.setPicUrl(albumPojo.getPic());
            recommendAlbumNewRes.setDescription(albumPojo.getDescription());
            
            List<ArtistPojo> singerListByAlbumIds = qukuService.getArtistListByAlbumIds(albumPojo.getId());
            List<org.api.neteasecloudmusic.model.vo.recommend.albumnew.ArtistsItem> artists = new ArrayList<>();
            for (ArtistPojo singerListByAlbumId : singerListByAlbumIds) {
                org.api.neteasecloudmusic.model.vo.recommend.albumnew.ArtistsItem artistsItem = new org.api.neteasecloudmusic.model.vo.recommend.albumnew.ArtistsItem();
                artistsItem.setAlbumSize(0);
                artistsItem.setId(singerListByAlbumId.getId());
                artistsItem.setPicUrl(singerListByAlbumId.getPic());
                artistsItem.setName(singerListByAlbumId.getArtistName());
                artistsItem.setAlias(AliasUtil.getAliasList(singerListByAlbumId.getAliasName()));
                artistsItem.setBriefDesc(singerListByAlbumId.getIntroduction());
                artists.add(artistsItem);
            }
            recommendAlbumNewRes.setArtists(artists);
            
            if (CollUtil.isNotEmpty(artists)) {
                Artist artist = new Artist();
                BeanUtil.copyProperties(artists.get(0), artist);
                recommendAlbumNewRes.setArtist(artist);
            }
            recommendAlbumNewResPage.getRecords().add(recommendAlbumNewRes);
        }
        BeanUtils.copyProperties(albumPojoList, recommendAlbumNewResPage, "records");
        return recommendAlbumNewResPage;
    }
    
    
}
