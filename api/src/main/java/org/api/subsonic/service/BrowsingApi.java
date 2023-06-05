package org.api.subsonic.service;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.common.service.QukuAPI;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.album.Album;
import org.api.subsonic.model.res.album.AlbumRes;
import org.api.subsonic.model.res.album.SongItem;
import org.api.subsonic.model.res.song.Song;
import org.api.subsonic.model.res.song.SongRes;
import org.core.iservice.TbAlbumService;
import org.core.iservice.TbMusicService;
import org.core.model.convert.ArtistConvert;
import org.core.model.convert.MusicConvert;
import org.core.pojo.TbAlbumPojo;
import org.core.pojo.TbMusicPojo;
import org.core.pojo.TbMusicUrlPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service(SubsonicConfig.SUBSONIC + "BrowsingApi")
public class BrowsingApi {
    
    @Autowired
    private QukuAPI qukuService;
    
    @Autowired
    private TbAlbumService albumService;
    
    @Autowired
    private TbMusicService musicService;
    
    
    public AlbumRes getAlbum(Long id) {
        TbAlbumPojo albumPojo = albumService.getById(id);
        Album album = new Album();
        album.setId(String.valueOf(albumPojo.getId()));
        album.setName(albumPojo.getAlbumName());
        List<ArtistConvert> artistListByAlbumIds = qukuService.getAlbumArtistListByAlbumIds(albumPojo.getId());
        ArtistConvert artistPojo = CollUtil.isEmpty(artistListByAlbumIds) ? new ArtistConvert() : artistListByAlbumIds.get(0);
        album.setArtist(artistPojo.getArtistName());
        album.setArtistId(String.valueOf(artistPojo.getId()));
        album.setCoverArt(String.valueOf(albumPojo.getId()));
        album.setSongCount(qukuService.getAlbumMusicCountByAlbumId(albumPojo.getId()));
        album.setPlayCount(0);
        album.setPlayed(albumPojo.getUpdateTime().toString());
        album.setCreated(albumPojo.getCreateTime().toString());
        album.setYear(albumPojo.getPublishTime().getYear());
        album.setStarred(albumPojo.getUpdateTime().toString());
    
        ArrayList<SongItem> song = new ArrayList<>();
        List<MusicConvert> musicListByAlbumId = qukuService.getMusicListByAlbumId(id);
        List<ArtistConvert> artistListByAlbumIds1 = qukuService.getAlbumArtistListByAlbumIds(id);
        ArtistConvert tbArtistPojo = CollUtil.isEmpty(artistListByAlbumIds1) ? new ArtistConvert() : artistListByAlbumIds1.get(0);
        Map<Long, List<TbMusicUrlPojo>> musicMapUrl = qukuService.getMusicMapUrl(musicListByAlbumId.stream()
                                                                                                   .map(TbMusicPojo::getId)
                                                                                                   .collect(Collectors.toSet()));
        int duration = 0;
        for (TbMusicPojo musicPojo : musicListByAlbumId) {
            SongItem e = new SongItem();
            e.setId(String.valueOf(musicPojo.getId()));
            e.setParent(String.valueOf(albumPojo.getId()));
            e.setIsDir(false);
            e.setTitle(musicPojo.getMusicName());
            e.setAlbum(albumPojo.getAlbumName());
            e.setAlbumId(String.valueOf(albumPojo.getId()));
            e.setArtist(tbArtistPojo.getArtistName());
            e.setArtistId(String.valueOf(tbArtistPojo.getId()));
            e.setTrack(1);
            e.setYear(albumPojo.getPublishTime().getYear());
            e.setCoverArt(String.valueOf(musicPojo.getId()));
            List<TbMusicUrlPojo> musicUrl = musicMapUrl.get(musicPojo.getId());
            TbMusicUrlPojo tbMusicUrlPojo = CollUtil.isEmpty(musicUrl) ? new TbMusicUrlPojo() : musicUrl.get(0);
            e.setSize(Math.toIntExact(tbMusicUrlPojo.getSize() == null ? 0 : tbMusicUrlPojo.getSize()));
            e.setContentType("audio/" + tbMusicUrlPojo.getEncodeType());
            e.setSuffix(tbMusicUrlPojo.getEncodeType());
            e.setStarred(musicPojo.getUpdateTime().toString());
            e.setDuration(Optional.ofNullable(musicPojo.getTimeLength()).orElse(0) / 1000);
            e.setBitRate(tbMusicUrlPojo.getRate());
            e.setPath(tbMusicUrlPojo.getUrl());
            e.setPlayCount(0);
            e.setPlayed(musicPojo.getCreateTime().toString());
            e.setType("music");
            e.setIsVideo(false);
            e.setCreated(albumPojo.getPublishTime().toString());
            song.add(e);
            duration += musicPojo.getTimeLength();
        }
        album.setDuration(duration / 1000);
        album.setSong(song);
        
        
        AlbumRes albumRes = new AlbumRes();
        albumRes.setAlbum(album);
        return albumRes;
    }
    
    public SongRes getSong(Long id) {
        TbMusicPojo musicPojo = musicService.getById(id);
        TbAlbumPojo albumByAlbumId = qukuService.getAlbumByAlbumId(musicPojo.getAlbumId());
        List<ArtistConvert> artistByMusicId = qukuService.getAlbumArtistByMusicId(musicPojo.getId());
        List<TbMusicUrlPojo> musicUrl = qukuService.getMusicPaths(CollUtil.newHashSet(musicPojo.getId()));
        TbMusicUrlPojo tbMusicUrlPojo = CollUtil.isEmpty(musicUrl) ? new TbMusicUrlPojo() : musicUrl.get(0);
        ArtistConvert tbArtistPojo = CollUtil.isEmpty(artistByMusicId) ? new ArtistConvert() : artistByMusicId.get(0);
        Song song = new Song();
        song.setId(String.valueOf(musicPojo.getId()));
        song.setIsDir(false);
        song.setTitle(musicPojo.getMusicName());
        song.setAlbum(albumByAlbumId.getAlbumName());
        song.setArtist(tbArtistPojo.getArtistName());
        song.setArtistId(String.valueOf(tbArtistPojo.getId()));
        song.setTrack(0);
        song.setYear(albumByAlbumId.getPublishTime().getYear());
        song.setCoverArt(String.valueOf(musicPojo.getPicId()));
        song.setSize(Math.toIntExact((tbMusicUrlPojo.getSize() == null ? 0 : tbMusicUrlPojo.getSize())));
        song.setContentType("audio/mpeg");
        song.setSuffix(tbMusicUrlPojo.getEncodeType());
        song.setStarred(musicPojo.getUpdateTime().toString());
        song.setDuration(musicPojo.getTimeLength() / 1000);
        song.setBitRate(tbMusicUrlPojo.getRate());
        song.setPath(tbMusicUrlPojo.getUrl());
        song.setPlayCount(0);
        song.setPlayed(musicPojo.getUpdateTime().toString());
        song.setCreated(musicPojo.getCreateTime().toString());
        song.setType("music");
        song.setUserRating(1);
        song.setIsVideo(false);
        
        SongRes songRes = new SongRes();
        songRes.setSong(song);
        return songRes;
    }
}
