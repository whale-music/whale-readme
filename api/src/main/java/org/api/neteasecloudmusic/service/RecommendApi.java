package org.api.neteasecloudmusic.service;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.common.service.MusicCommonApi;
import org.api.neteasecloudmusic.model.vo.personalfm.Album;
import org.api.neteasecloudmusic.model.vo.personalfm.ArtistsItem;
import org.api.neteasecloudmusic.model.vo.personalfm.DataItem;
import org.api.neteasecloudmusic.model.vo.personalfm.PersonalFMRes;
import org.api.neteasecloudmusic.model.vo.personalized.PersonalizedRes;
import org.api.neteasecloudmusic.model.vo.personalized.ResultItem;
import org.core.pojo.*;
import org.core.service.PlayListService;
import org.core.service.QukuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("RecommendApi")
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
            e.setName(tbMusicPojo.getMusicName());
            e.setAlias(List.of(tbMusicPojo.getAliaName().split(",")));
            // 歌曲下载地址
            List<TbMusicUrlPojo> musicUrlByMusicId = musicCommonApi.getMusicUrlByMusicId(tbMusicPojo.getId());
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
            List<TbSingerPojo> singerByMusicId = qukuService.getSingerByMusicId(tbMusicPojo.getId());
            int albumSize = qukuService.getAlbumMusicSizeByAlbumId(tbMusicPojo.getAlbumId());
            for (TbSingerPojo tbSingerPojo : singerByMusicId) {
                ArtistsItem artistsItem = new ArtistsItem();
                artistsItem.setName(tbSingerPojo.getSingerName());
                artistsItem.setAlbumSize(albumSize);
                artistsItem.setId(tbSingerPojo.getId());
                artistsItem.setImg1v1Url(tbSingerPojo.getPic());
                artistsItem.setPicUrl(tbSingerPojo.getPic());
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
}
