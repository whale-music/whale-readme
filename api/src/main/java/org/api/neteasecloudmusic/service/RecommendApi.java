package org.api.neteasecloudmusic.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
            TbMusicPojo tbMusicPojo = qukuService.randomMusic();
            // 无数据直接返回
            if (tbMusicPojo == null) {
                return res;
            }
            // 填充数据
            DataItem e = new DataItem();
            // 歌曲信息
            e.setId(tbMusicPojo.getId());
            e.setName(tbMusicPojo.getMusicName());
            e.setAlias(List.of(tbMusicPojo.getAliasName().split(",")));
            // 歌曲下载地址
            List<TbMusicUrlPojo> musicUrlByMusicId = musicCommonApi.getMusicUrlByMusicId(tbMusicPojo.getId(), false);
            if (CollUtil.isNotEmpty(musicUrlByMusicId)) {
                e.setMp3Url(musicUrlByMusicId.get(0).getUrl());
            }
            e.setDuration(tbMusicPojo.getTimeLength());
            
            // 专辑信息
            TbAlbumPojo byId = qukuService.getAlbumByAlbumId(tbMusicPojo.getAlbumId());
            Album album = new Album();
            album.setName(byId.getAlbumName());
            album.setId(byId.getId());
            album.setPicUrl(byId.getPic());
            e.setAlbum(album);
            
            // 歌手信息
            ArrayList<ArtistsItem> artists = new ArrayList<>();
            List<TbArtistPojo> singerByMusicId = qukuService.getArtistByMusicId(tbMusicPojo.getId());
            int albumSize = qukuService.getAlbumMusicCountByAlbumId(tbMusicPojo.getAlbumId());
            for (TbArtistPojo tbArtistPojo : singerByMusicId) {
                ArtistsItem artistsItem = new ArtistsItem();
                artistsItem.setName(tbArtistPojo.getArtistName());
                artistsItem.setAlbumSize(albumSize);
                artistsItem.setId(tbArtistPojo.getId());
                artistsItem.setImg1v1Url(tbArtistPojo.getPic());
                artistsItem.setPicUrl(tbArtistPojo.getPic());
                artists.add(artistsItem);
            }
            e.setArtists(artists);
            res.getData().add(e);
        }
        
        return res;
    }
    
    
    public PersonalizedRes personalized(Long limit) {
        List<TbCollectPojo> tbCollectPojos = playListService.randomPlayList(limit);
        PersonalizedRes personalizedRes = new PersonalizedRes();
        List<ResultItem> result = new ArrayList<>();
        for (TbCollectPojo tbCollectPojo : tbCollectPojos) {
            ResultItem e = new ResultItem();
            e.setId(tbCollectPojo.getId());
            e.setName(tbCollectPojo.getPlayListName());
            e.setPicUrl(tbCollectPojo.getPic());
            e.setCanDislike(true);
            e.setTrackNumberUpdateTime(tbCollectPojo.getUpdateTime().getNano());
            result.add(e);
        }
        personalizedRes.setResult(result);
        return personalizedRes;
    }
    
    public List<DailyRecommendResourceRes> recommendResource(int limit) {
        List<TbCollectPojo> tbCollectPojos = playListService.randomPlayList((long) limit);
        List<DailyRecommendResourceRes> res = new ArrayList<>();
        for (TbCollectPojo tbCollectPojo : tbCollectPojos) {
            DailyRecommendResourceRes e = new DailyRecommendResourceRes();
            e.setId(tbCollectPojo.getId());
            e.setName(tbCollectPojo.getPlayListName());
            e.setPicUrl(tbCollectPojo.getPic());
            e.setPlaycount(3000L);
            e.setCreateTime((long) tbCollectPojo.getCreateTime().getNano());
            e.setUserId(tbCollectPojo.getUserId());
            
            // 创建者信息
            SysUserPojo userPojo = accountService.getById(tbCollectPojo.getUserId());
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
            TbMusicPojo tbMusicPojo = qukuService.randomMusic();
            DailySongsItem dailySongsItem = new DailySongsItem();
            dailySongsItem.setName(tbMusicPojo.getMusicName());
            dailySongsItem.setId(tbMusicPojo.getId());
            dailySongsItem.setPublishTime(tbMusicPojo.getCreateTime().getNano());
            dailySongsItem.setReason(reason);
            dailySongsItem.setRecommendReason(reason);
    
            // 歌手信息
            ArrayList<org.api.neteasecloudmusic.model.vo.recommend.songs.Artist> artists = new ArrayList<>();
            List<ArItem> ar = new ArrayList<>();
            List<TbArtistPojo> singerByMusicId = qukuService.getArtistByMusicId(tbMusicPojo.getId());
            for (TbArtistPojo tbArtistPojo : singerByMusicId) {
                ArItem arItem = new ArItem();
                arItem.setName(tbArtistPojo.getArtistName());
                arItem.setId(tbArtistPojo.getId());
                arItem.setAlias(AliasUtil.getAliasList(tbArtistPojo.getAliasName()));
                ar.add(arItem);
    
                // 兼容web api
                org.api.neteasecloudmusic.model.vo.recommend.songs.Artist artist = new org.api.neteasecloudmusic.model.vo.recommend.songs.Artist();
                artist.setName(tbArtistPojo.getArtistName());
                artist.setId(tbArtistPojo.getId());
                artist.setPicUrl(tbArtistPojo.getPic());
                artist.setAlias(AliasUtil.getAliasList(tbArtistPojo.getAliasName()));
                artist.setMusicSize(qukuService.getMusicCountBySingerId(tbArtistPojo.getId()));
                artist.setAlbumSize(qukuService.getAlbumCountBySingerId(tbMusicPojo.getId()));
                artists.add(artist);
            }
            dailySongsItem.setAr(ar);
            dailySongsItem.setArtists(artists);
    
    
            TbAlbumPojo albumByAlbumId = qukuService.getAlbumByAlbumId(tbMusicPojo.getAlbumId());
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
            privilege.setId(tbMusicPojo.getId());
            dailySongsItem.setPrivilege(privilege);
            dailySongsItems.add(dailySongsItem);
    
    
            RecommendReasonsItem recommendReasonsItem = new RecommendReasonsItem();
            recommendReasonsItem.setSongId(tbMusicPojo.getId());
            recommendReasonsItem.setReason(reason);
            recommendReasons.add(recommendReasonsItem);
        }
        
        res.setRecommendReasons(recommendReasons);
        res.setDailySongs(dailySongsItems);
        return res;
    }
    
    public Page<RecommendAlbumNewRes> albumNew(String area, Long offset, Long limit) {
        Page<TbAlbumPojo> albumPojoList = qukuService.getAlbumPage(area, offset, limit);
        Page<RecommendAlbumNewRes> recommendAlbumNewResPage = new Page<>();
        recommendAlbumNewResPage.setRecords(new ArrayList<>());
        for (TbAlbumPojo albumPojo : albumPojoList.getRecords()) {
            RecommendAlbumNewRes recommendAlbumNewRes = new RecommendAlbumNewRes();
            recommendAlbumNewRes.setId(albumPojo.getId());
            recommendAlbumNewRes.setSize(qukuService.getAlbumCountBySingerId(albumPojo.getId()));
            recommendAlbumNewRes.setPublishTime((long) albumPojo.getCreateTime().getNano());
            recommendAlbumNewRes.setName(albumPojo.getAlbumName());
            recommendAlbumNewRes.setPicUrl(albumPojo.getPic());
            recommendAlbumNewRes.setDescription(albumPojo.getDescription());
    
            List<TbArtistPojo> singerListByAlbumIds = qukuService.getArtistListByAlbumIds(albumPojo.getId());
            List<org.api.neteasecloudmusic.model.vo.recommend.albumnew.ArtistsItem> artists = new ArrayList<>();
            for (TbArtistPojo singerListByAlbumId : singerListByAlbumIds) {
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
