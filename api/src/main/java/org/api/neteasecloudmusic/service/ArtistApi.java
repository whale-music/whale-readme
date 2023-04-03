package org.api.neteasecloudmusic.service;

import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.artist.album.Artist;
import org.api.neteasecloudmusic.model.vo.artist.album.ArtistAlbumRes;
import org.api.neteasecloudmusic.model.vo.artist.album.ArtistsItem;
import org.api.neteasecloudmusic.model.vo.artist.album.HotAlbumsItem;
import org.api.neteasecloudmusic.model.vo.artist.artist.Al;
import org.api.neteasecloudmusic.model.vo.artist.artist.ArItem;
import org.api.neteasecloudmusic.model.vo.artist.artist.ArtistRes;
import org.api.neteasecloudmusic.model.vo.artist.artist.HotSongsItem;
import org.api.neteasecloudmusic.model.vo.artist.sublist.ArtistSubListRes;
import org.api.neteasecloudmusic.model.vo.artist.sublist.DataItem;
import org.core.common.page.Page;
import org.core.common.page.Wrappers;
import org.core.iservice.AlbumArtistService;
import org.core.iservice.AlbumService;
import org.core.iservice.ArtistService;
import org.core.pojo.*;
import org.core.service.QukuService;
import org.core.utils.AliasUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "ArtistApi")
public class ArtistApi {
    @Autowired
    private QukuService qukuService;
    
    @Autowired
    private AlbumArtistService albumArtistService;
    
    @Autowired
    private AlbumService albumService;
    
    @Autowired
    private ArtistService artistService;
    
    public ArtistSubListRes artistSublist(SysUserPojo user) {
        ArtistSubListRes res = new ArtistSubListRes();
        List<ArtistPojo> userPojoList = qukuService.getUserLikeSingerList(user);
        ArrayList<DataItem> data = new ArrayList<>();
        for (ArtistPojo artistPojo : userPojoList) {
            DataItem e = new DataItem();
            e.setName(artistPojo.getArtistName());
            e.setId(artistPojo.getId());
            String singerAlias = Optional.ofNullable(artistPojo.getAliasName()).orElse("");
            e.setAlias(Arrays.asList(singerAlias.split(",")));
            e.setPicUrl(artistPojo.getPic());
            e.setAlbumSize(qukuService.getAlbumCountBySingerId(e.getId()));
            e.setMvSize(0);
            data.add(e);
        }
        res.setData(data);
        return res;
    }
    
    public ArtistAlbumRes artistAlbum(Long id, Long limit, Long offset) {
        Page<AlbumArtistPojo> page = new Page<>(offset, limit);
        albumArtistService.page(page, Wrappers.<AlbumArtistPojo>lambdaQuery().eq(AlbumArtistPojo::getArtistId, id));
        List<Long> albumIds = page.getRecords().stream().map(AlbumArtistPojo::getAlbumId).collect(Collectors.toList());
        List<AlbumPojo> albumPojoList = albumService.listByIds(albumIds);
        
        Artist artist = new Artist();
        ArtistPojo singerPojo = artistService.getById(id);
        artist.setImg1v1Url(singerPojo.getPic());
        artist.setId(singerPojo.getId());
        artist.setName(singerPojo.getArtistName());
        artist.setAlias(AliasUtil.getAliasList(singerPojo.getArtistName()));
        artist.setPicIdStr(singerPojo.getPic());
        artist.setPicUrl(singerPojo.getPic());
        artist.setMusicSize(qukuService.getMusicCountBySingerId(singerPojo.getId()));
        artist.setAlbumSize(qukuService.getAlbumCountBySingerId(singerPojo.getId()));
        
        ArtistsItem e1 = new ArtistsItem();
        e1.setPicUrl(singerPojo.getPic());
        e1.setId(singerPojo.getId());
        e1.setName(singerPojo.getArtistName());
        e1.setImg1v1Url(singerPojo.getPic());
        
        ArrayList<ArtistsItem> artists = new ArrayList<>();
        artists.add(e1);
        
        
        ArrayList<HotAlbumsItem> hotAlbums = new ArrayList<>();
        for (AlbumPojo albumPojo : albumPojoList) {
            HotAlbumsItem e = new HotAlbumsItem();
            e.setId(albumPojo.getId());
            e.setName(albumPojo.getAlbumName());
            e.setCompany(albumPojo.getCompany());
            e.setPicUrl(albumPojo.getPic());
            e.setBlurPicUrl(albumPojo.getPic());
            e.setArtist(artist);
            
            e.setArtists(artists);
            hotAlbums.add(e);
        }
        ArtistAlbumRes res = new ArtistAlbumRes();
        res.setArtist(artist);
        res.setHotAlbums(hotAlbums);
        return res;
    }
    
    /**
     * 获取歌手(信息)单曲
     *
     * @param id 歌手ID
     */
    public ArtistRes artists(Long id) {
        ArtistRes artistRes = new ArtistRes();
        ArtistPojo singerPojo = artistService.getById(id);
        List<MusicPojo> musicPojoList = qukuService.getMusicListBySingerId(id);
        org.api.neteasecloudmusic.model.vo.artist.artist.Artist artist = new org.api.neteasecloudmusic.model.vo.artist.artist.Artist();
        artist.setName(singerPojo.getArtistName());
        artist.setId(singerPojo.getId());
        artist.setPicUrl(singerPojo.getPic());
        artist.setAlias(AliasUtil.getAliasList(singerPojo.getAliasName()));
        artist.setImg1v1IdStr(singerPojo.getPic());
        artist.setMusicSize(qukuService.getMusicCountBySingerId(singerPojo.getId()));
        artist.setBriefDesc(singerPojo.getIntroduction());
        artist.setImg1v1Url(singerPojo.getPic());
        artistRes.setArtist(artist);
        
        ArrayList<HotSongsItem> hotSongs = new ArrayList<>();
        for (MusicPojo musicPojo : musicPojoList) {
            HotSongsItem hotSongsItem = new HotSongsItem();
            hotSongsItem.setName(musicPojo.getMusicName());
            hotSongsItem.setAlia(AliasUtil.getAliasList(musicPojo.getAliasName()));
            hotSongsItem.setId(musicPojo.getId());
            
            AlbumPojo albumByMusicId = qukuService.getAlbumByMusicId(musicPojo.getId());
            Al al = new Al();
            al.setId(albumByMusicId.getId());
            al.setName(albumByMusicId.getAlbumName());
            al.setPicUrl(albumByMusicId.getPic());
            hotSongsItem.setAl(al);
            hotSongs.add(hotSongsItem);
            
            ArrayList<ArItem> ar = new ArrayList<>();
            List<ArtistPojo> singerByMusicId = qukuService.getSingerByMusicId(musicPojo.getId());
            for (ArtistPojo artistPojo : singerByMusicId) {
                ArItem arItem = new ArItem();
                arItem.setId(artistPojo.getId());
                arItem.setAlia(AliasUtil.getAliasList(artistPojo.getAliasName()));
                arItem.setName(artistPojo.getArtistName());
                ar.add(arItem);
            }
            hotSongsItem.setAr(ar);
        }
        artistRes.setHotSongs(hotSongs);
        
        
        artistRes.setMore(true);
        return artistRes;
    }
}
