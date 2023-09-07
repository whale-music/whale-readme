package org.api.subsonic.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.api.common.service.QukuAPI;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.playlist.EntryItem;
import org.api.subsonic.model.res.playlist.PlayList;
import org.api.subsonic.model.res.playlist.PlaylistRes;
import org.api.subsonic.model.res.playlists.PlayLists;
import org.api.subsonic.model.res.playlists.PlaylistItem;
import org.api.subsonic.model.res.playlists.PlaylistsRes;
import org.api.subsonic.model.res.starred2.Starred2;
import org.api.subsonic.model.res.starred2.Starred2Res;
import org.core.common.exception.BaseException;
import org.core.config.PlayListTypeConfig;
import org.core.mybatis.iservice.TbCollectService;
import org.core.mybatis.iservice.TbResourceService;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.CollectConvert;
import org.core.mybatis.pojo.*;
import org.core.service.AccountService;
import org.core.service.PlayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service(SubsonicConfig.SUBSONIC + "PlaylistApi")
public class PlaylistApi {
    
    @Autowired
    private QukuAPI qukuApi;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private PlayListService playListService;
    
    @Autowired
    private TbCollectService collectService;
    
    @Autowired
    private TbResourceService tbResourceService;
    
    public PlaylistsRes getPlaylists(SubsonicCommonReq req, String username) {
        username = StringUtils.isBlank(username) ? req.getU() : username;
        SysUserPojo user = accountService.getUser(username);
        List<CollectConvert> userPlayList = qukuApi.getUserPlayList(user.getId(),
                Arrays.asList(PlayListTypeConfig.ORDINARY, PlayListTypeConfig.ORDINARY));
        
        List<PlaylistItem> playlist = new ArrayList<>();
        for (TbCollectPojo collectPojo : userPlayList) {
            PlaylistItem e = new PlaylistItem();
            e.setId(String.valueOf(collectPojo.getId()));
            e.setName(collectPojo.getPlayListName());
            e.setChanged(LocalDateTimeUtil.format(collectPojo.getUpdateTime(), DatePattern.NORM_DATETIME_FORMATTER));
            e.setSongCount(qukuApi.getCollectMusicCount(collectPojo.getId()));
            e.setCreated(LocalDateTimeUtil.format(collectPojo.getCreateTime(), DatePattern.NORM_DATETIME_FORMATTER));
            e.setCoverArt(String.valueOf(collectPojo.getId()));
            e.setOwner(user.getUsername());
            playlist.add(e);
        }
        PlayLists playlists = new PlayLists();
        playlists.setPlaylist(playlist);
        PlaylistsRes playlistRes = new PlaylistsRes();
        playlistRes.setPlaylists(playlists);
        return playlistRes;
    }
    
    /**
     * 获取歌单数据
     *
     * @param id 歌单ID
     * @return 返回歌单信息
     */
    public PlaylistRes getPlaylist(Long id) {
        List<TbMusicPojo> playListAllMusic = playListService.getPlayListAllMusic(id);
        
        ArrayList<EntryItem> entry = new ArrayList<>();
        int duration = 0;
        for (TbMusicPojo musicPojo : playListAllMusic) {
            EntryItem e = new EntryItem();
            List<TbResourcePojo> musicUrl = qukuApi.getMusicPaths(CollUtil.newHashSet(musicPojo.getId()));
            e.setId(String.valueOf(musicPojo.getId()));
            e.setTitle(musicPojo.getMusicName());
            TbResourcePojo tbMusicUrlPojo = CollUtil.isEmpty(musicUrl) ? new TbResourcePojo() : musicUrl.get(0);
            e.setBitRate(tbMusicUrlPojo.getRate() == null ? 0 : tbMusicUrlPojo.getRate());
            e.setIsDir(false);
            e.setCoverArt(String.valueOf(musicPojo.getId()));
            e.setPlayed(musicPojo.getCreateTime().toString());
            
            TbAlbumPojo albumByAlbumId = Optional.ofNullable(qukuApi.getAlbumByAlbumId(musicPojo.getAlbumId())).orElse(new AlbumConvert());
            e.setAlbum(albumByAlbumId.getAlbumName());
            e.setAlbumId(String.valueOf(albumByAlbumId.getId()));
            e.setParent(String.valueOf(albumByAlbumId.getId()));
            
            // 流派
            e.setGenre("");
            e.setTrack(0);
            LocalDateTime publishTime = albumByAlbumId.getPublishTime();
            e.setYear(publishTime == null ? null : publishTime.getYear());
            e.setDuration(musicPojo.getTimeLength() / 1000);
            e.setSize(Math.toIntExact(tbMusicUrlPojo.getSize() == null ? 0 : tbMusicUrlPojo.getSize()));
            e.setSuffix(tbMusicUrlPojo.getEncodeType());
            e.setType("music");
            e.setContentType("audio/mpeg");
            e.setParent(tbMusicUrlPojo.getPath());
            e.setPlayCount(0);
            
            List<ArtistConvert> artistByMusicId = qukuApi.getAlbumArtistByMusicId(musicPojo.getId());
            TbArtistPojo artistPojo = CollUtil.isEmpty(artistByMusicId) ? new TbArtistPojo() : artistByMusicId.get(0);
            e.setArtist(artistPojo.getArtistName());
            e.setArtistId(String.valueOf(artistPojo.getId()));
            
            e.setVideo(false);
            entry.add(e);
            duration += e.getDuration();
        }
    
        TbCollectPojo byId = collectService.getById(id);
        PlayList playlistRes = new PlayList();
        playlistRes.setId(String.valueOf(byId.getId()));
        playlistRes.setName(byId.getPlayListName());
        playlistRes.setSongCount(qukuApi.getCollectMusicCount(byId.getId()));
        playlistRes.setDuration(duration / 1000);
        playlistRes.setJsonMemberPublic(true);
        SysUserPojo byId1 = accountService.getById(byId.getUserId());
        playlistRes.setOwner(Optional.ofNullable(byId1).orElse(new SysUserPojo()).getUsername());
        playlistRes.setCreated(byId.getCreateTime().toString());
        playlistRes.setChanged(byId.getUpdateTime().toString());
        playlistRes.setCoverArt(String.valueOf(byId.getId()));
        
        playlistRes.setEntry(entry);
        PlaylistRes playlistRes1 = new PlaylistRes();
        playlistRes1.setPlaylist(playlistRes);
        return playlistRes1;
    }
    
    
    public Starred2Res getStarred2(SubsonicCommonReq req) {
        SysUserPojo userByName = accountService.getUserByName(req.getU());
        List<CollectConvert> userPlayList = qukuApi.getUserPlayList(userByName.getId(), Collections.singletonList(PlayListTypeConfig.LIKE));
        if (CollUtil.isEmpty(userPlayList) || userPlayList.size() != 1) {
            throw new BaseException();
        }
        Starred2Res starred2Res = new Starred2Res();
        
        Starred2 starred2 = new Starred2();
        ArrayList<Starred2.Song> songList = new ArrayList<>();
        CollectConvert likeCollect = userPlayList.get(0);
        List<TbMusicPojo> likeMusicList = playListService.getPlayListAllMusic(likeCollect.getId());
        // 歌手
        List<Long> musicIds = likeMusicList.parallelStream().map(TbMusicPojo::getId).toList();
        Map<Long, List<ArtistConvert>> musicArtistByMusicIdToMap = qukuApi.getMusicArtistByMusicIdToMap(musicIds);
        // 专辑
        Map<Long, AlbumConvert> musicAlbumByMusicIdToMap = qukuApi.getMusicAlbumByMusicIdToMap(likeMusicList.parallelStream()
                                                                                                            .map(TbMusicPojo::getAlbumId)
                                                                                                            .toList());
        
        // 音乐资源
        Map<Long, List<TbResourcePojo>> resourceList = tbResourceService.getResourceList(musicIds);
        for (TbMusicPojo tbMusicPojo : likeMusicList) {
            Starred2.Song song = new Starred2.Song();
            song.setId(String.valueOf(tbMusicPojo.getId()));
            song.setDir(false);
            song.setTitle(tbMusicPojo.getMusicName());
            song.setTrack(0);
            song.setStarred(Date.from(tbMusicPojo.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
            song.setDuration(tbMusicPojo.getTimeLength());
            song.setPlayCount(1);
            song.setPlayed(new Date());
            song.setDiscNumber(1);
            song.setType("music");
            song.setUserRating(0);
            song.setVideo(false);
            // 封面
            song.setCoverArt(String.valueOf(tbMusicPojo.getId()));
            
            // 音乐资源
            List<TbResourcePojo> collection = resourceList.get(tbMusicPojo.getId());
            if (CollUtil.isNotEmpty(collection)) {
                TbResourcePojo tbResourcePojo = collection.get(0);
                song.setSize(tbResourcePojo.getSize());
                song.setContentType("audio/" + tbResourcePojo.getEncodeType());
                song.setSuffix(tbResourcePojo.getEncodeType());
                song.setBitRate(tbResourcePojo.getRate());
                song.setPath(tbResourcePojo.getPath());
            }
            
            AlbumConvert albumConvert = musicAlbumByMusicIdToMap.get(tbMusicPojo.getAlbumId());
            // 专辑
            if (Objects.nonNull(albumConvert)) {
                song.setAlbum(albumConvert.getAlbumName());
                song.setAlbumId(String.valueOf(albumConvert.getId()));
                song.setYear(Optional.ofNullable(albumConvert.getPublishTime()).orElse(LocalDateTime.now()).getYear());
                song.setParent(String.valueOf(albumConvert.getId()));
            }
            
            List<ArtistConvert> artistConverts = musicArtistByMusicIdToMap.get(tbMusicPojo.getId());
            // 歌手信息
            if (CollUtil.isNotEmpty(artistConverts)) {
                ArtistConvert artistConvert = artistConverts.get(0);
                song.setArtistId(String.valueOf(artistConvert.getId()));
                song.setArtist(artistConvert.getArtistName());
            }
            
            songList.add(song);
        }
        starred2.setSong(songList);
        ArrayList<Starred2.Album> album = new ArrayList<>();
        List<Long> albumIds = musicAlbumByMusicIdToMap.values().parallelStream().filter(Objects::nonNull).map(TbAlbumPojo::getId).toList();
        Map<Long, List<TbTagPojo>> labelAlbumGenre = qukuApi.getLabelAlbumGenre(albumIds);
        
        Map<Long, List<ArtistConvert>> albumArtistMapByAlbumIds = qukuApi.getAlbumArtistMapByAlbumIds(albumIds);
        for (AlbumConvert value : musicAlbumByMusicIdToMap.values()) {
            Starred2.Album e = new Starred2.Album();
            e.setId(String.valueOf(value.getId()));
            e.setDir(true);
            e.setTitle(value.getAlbumName());
            e.setAlbum(value.getAlbumName());
            e.setYear(Optional.ofNullable(value.getPublishTime()).orElse(LocalDateTime.now()).getYear());
            e.setCoverArt(String.valueOf(value.getId()));
            e.setStarred(Date.from(value.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
            // 专辑所有音乐时长
            e.setDuration(0);
            e.setPlayCount(0);
            e.setPlayed(new Date());
            e.setCreated(Date.from(value.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
            e.setUserRating(0);
            e.setSongCount(qukuApi.getAlbumMusicCountByAlbumId(value.getId()));
            e.setVideo(false);
            
            List<TbTagPojo> tbTagPojos = labelAlbumGenre.get(value.getId());
            if (CollUtil.isNotEmpty(tbTagPojos)) {
                e.setGenre(tbTagPojos.get(0).getTagName());
            }
            
            // 歌手
            List<ArtistConvert> collection = albumArtistMapByAlbumIds.get(value.getId());
            if (CollUtil.isNotEmpty(collection)) {
                ArtistConvert artistConvert = collection.get(0);
                e.setParent(String.valueOf(artistConvert.getId()));
                e.setArtist(artistConvert.getArtistName());
                e.setArtistId(String.valueOf(artistConvert.getId()));
            }
            
            album.add(e);
        }
        starred2.setAlbum(album);
        
        ArrayList<Starred2.Artist> artist = new ArrayList<>();
        
        HashSet<ArtistConvert> artistConverts = new HashSet<>();
        for (List<ArtistConvert> value : musicArtistByMusicIdToMap.values()) {
            artistConverts.addAll(value);
        }
        for (ArtistConvert artistConvert : artistConverts) {
            Starred2.Artist e = new Starred2.Artist();
            e.setId(String.valueOf(artistConvert.getId()));
            e.setCoverArt(String.valueOf(artistConvert.getId()));
            e.setName(artistConvert.getArtistName());
            e.setUserRating(0);
            e.setArtistImageUrl(artistConvert.getPicUrl());
            e.setAlbumCount(qukuApi.getAlbumCountBySingerId(artistConvert.getId()));
            e.setStarred(Date.from(artistConvert.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
            artist.add(e);
        }
        starred2.setArtist(artist);
        
        starred2Res.setStarred2(starred2);
        return starred2Res;
    }
}
