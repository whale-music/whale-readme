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
import org.core.config.PlayListTypeConfig;
import org.core.mybatis.iservice.TbCollectService;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.CollectConvert;
import org.core.mybatis.pojo.*;
import org.core.service.AccountService;
import org.core.service.PlayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service(SubsonicConfig.SUBSONIC + "PlaylistApi")
public class PlaylistApi {
    
    @Autowired
    private QukuAPI qukuService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private PlayListService playListService;
    
    @Autowired
    private TbCollectService collectService;
    
    public PlaylistsRes getPlaylists(SubsonicCommonReq req, String username) {
        username = StringUtils.isBlank(username) ? req.getU() : username;
        SysUserPojo user = accountService.getUser(username);
        List<CollectConvert> userPlayList = qukuService.getUserPlayList(user.getId(),
                Arrays.asList(PlayListTypeConfig.ORDINARY, PlayListTypeConfig.ORDINARY));
    
        List<PlaylistItem> playlist = new ArrayList<>();
        for (TbCollectPojo collectPojo : userPlayList) {
            PlaylistItem e = new PlaylistItem();
            e.setId(String.valueOf(collectPojo.getId()));
            e.setName(collectPojo.getPlayListName());
            e.setChanged(LocalDateTimeUtil.format(collectPojo.getUpdateTime(), DatePattern.NORM_DATETIME_FORMATTER));
            e.setSongCount(qukuService.getCollectMusicCount(collectPojo.getId()));
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
            List<TbResourcePojo> musicUrl = qukuService.getMusicPaths(CollUtil.newHashSet(musicPojo.getId()));
            e.setId(String.valueOf(musicPojo.getId()));
            e.setTitle(musicPojo.getMusicName());
            TbResourcePojo tbMusicUrlPojo = CollUtil.isEmpty(musicUrl) ? new TbResourcePojo() : musicUrl.get(0);
            e.setBitRate(tbMusicUrlPojo.getRate() == null ? 0 : tbMusicUrlPojo.getRate());
            e.setIsDir(false);
            e.setCoverArt(String.valueOf(musicPojo.getId()));
            e.setPlayed(musicPojo.getCreateTime().toString());
    
            TbAlbumPojo albumByAlbumId = Optional.ofNullable(qukuService.getAlbumByAlbumId(musicPojo.getAlbumId())).orElse(new AlbumConvert());
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
    
            List<ArtistConvert> artistByMusicId = qukuService.getAlbumArtistByMusicId(musicPojo.getId());
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
        playlistRes.setSongCount(qukuService.getCollectMusicCount(byId.getId()));
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
}
