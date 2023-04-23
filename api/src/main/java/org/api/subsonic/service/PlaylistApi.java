package org.api.subsonic.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.PlayListTypeConfig;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.playlist.EntryItem;
import org.api.subsonic.model.res.playlist.Playlist;
import org.api.subsonic.model.res.playlist.PlaylistRes;
import org.api.subsonic.model.res.playlists.Playlists;
import org.api.subsonic.model.res.playlists.PlaylistsRes;
import org.core.iservice.TbCollectService;
import org.core.pojo.*;
import org.core.service.AccountService;
import org.core.service.PlayListService;
import org.core.service.QukuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(SubsonicConfig.SUBSONIC + "PlaylistApi")
public class PlaylistApi {
    
    @Autowired
    private QukuService qukuService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private PlayListService playListService;
    
    @Autowired
    private TbCollectService collectService;
    
    public PlaylistsRes getPlaylists(SubsonicCommonReq req, String username) {
        username = StringUtils.isBlank(username) ? req.getU() : username;
        SysUserPojo user = accountService.getUser(username);
        List<TbCollectPojo> userPlayList = qukuService.getUserPlayList(user.getId(),
                Arrays.asList(PlayListTypeConfig.ORDINARY, PlayListTypeConfig.ORDINARY));
        
        ArrayList<org.api.subsonic.model.res.playlists.Playlist> playlist = new ArrayList<>();
        for (TbCollectPojo collectPojo : userPlayList) {
            org.api.subsonic.model.res.playlists.Playlist e = new org.api.subsonic.model.res.playlists.Playlist();
            e.setId(String.valueOf(collectPojo.getId()));
            e.setName(collectPojo.getPlayListName());
            e.setChanged(LocalDateTimeUtil.format(collectPojo.getUpdateTime(), DatePattern.NORM_DATETIME_FORMATTER));
            e.setSongCount(qukuService.getCollectMusicCount(collectPojo.getId()));
            e.setCreated(LocalDateTimeUtil.format(collectPojo.getCreateTime(), DatePattern.NORM_DATETIME_FORMATTER));
            e.setCoverArt(String.valueOf(collectPojo.getId()));
            e.setOwner(user.getUsername());
            playlist.add(e);
        }
        Playlists playlists = new Playlists();
        playlists.setPlaylist(playlist);
        PlaylistsRes playlistRes = new PlaylistsRes();
        playlistRes.setPlaylists(playlists);
        return playlistRes;
    }
    
    /**
     * 获取歌单数据
     * @param req 访问用户信息
     * @param id 歌单ID
     * @return 返回歌单信息
     */
    public PlaylistRes getPlaylist(SubsonicCommonReq req, Long id) {
        List<TbMusicPojo> playListAllMusic = playListService.getPlayListAllMusic(id);
    
        ArrayList<EntryItem> entry = new ArrayList<>();
        for (TbMusicPojo musicPojo : playListAllMusic) {
            EntryItem e = new EntryItem();
            List<TbMusicUrlPojo> musicUrl = qukuService.getMusicUrl(Set.of(musicPojo.getId()));
            e.setId(String.valueOf(musicPojo.getId()));
            e.setTitle(musicPojo.getMusicName());
            e.setCoverArt(String.valueOf(musicPojo.getId()));
            TbMusicUrlPojo tbMusicUrlPojo = CollUtil.isEmpty(musicUrl) ? new TbMusicUrlPojo() : musicUrl.get(0);
            e.setBitRate(tbMusicUrlPojo.getRate());
            // 流派
            e.setGenre("");
            e.setDuration(musicPojo.getTimeLength());
            e.setSize(Math.toIntExact(tbMusicUrlPojo.getSize() == null ? 0 : tbMusicUrlPojo.getSize()));
            e.setSuffix(tbMusicUrlPojo.getEncodeType());
            e.setType("music");
            e.setContentType("audio/mpeg");
            
            TbAlbumPojo albumByAlbumId = Optional.ofNullable(qukuService.getAlbumByAlbumId(musicPojo.getAlbumId())).orElse(new TbAlbumPojo());
            e.setAlbum(albumByAlbumId.getAlbumName());
            e.setAlbumId(String.valueOf(albumByAlbumId.getId()));
            e.setParent(String.valueOf(albumByAlbumId.getId()));
            
            List<TbArtistPojo> artistByMusicId = qukuService.getArtistByMusicId(musicPojo.getId());
            TbArtistPojo artistPojo = CollUtil.isEmpty(artistByMusicId) ? new TbArtistPojo() : artistByMusicId.get(0);
            e.setArtist(artistPojo.getArtistName());
            e.setArtistId(String.valueOf(artistPojo.getId()));
            
            entry.add(e);
        }
    
        TbCollectPojo byId = collectService.getById(id);
        Playlist playlistRes = new Playlist();
        playlistRes.setName(byId.getPlayListName());
        playlistRes.setPublicStr(true);
        playlistRes.setCreated(byId.getCreateTime().toString());
        playlistRes.setChanged(byId.getUpdateTime().toString());
        SysUserPojo byId1 = accountService.getById(byId.getUserId());
        playlistRes.setOwner(byId1.getUsername());
        playlistRes.setSongCount(qukuService.getCollectMusicCount(byId.getId()));
        playlistRes.setId(String.valueOf(byId.getId()));
        
        playlistRes.setEntry(entry);
        PlaylistRes playlistRes1 = new PlaylistRes();
        playlistRes1.setPlaylist(playlistRes);
        return playlistRes1;
    }
}
