package org.api.subsonic.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.PlayListTypeConfig;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.playlists.Playlist;
import org.api.subsonic.model.res.playlists.PlaylistRes;
import org.api.subsonic.model.res.playlists.Playlists;
import org.core.pojo.SysUserPojo;
import org.core.pojo.TbCollectPojo;
import org.core.service.AccountService;
import org.core.service.QukuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service(SubsonicConfig.SUBSONIC + "PlaylistApi")
public class PlaylistApi {
    
    @Autowired
    private QukuService qukuService;
    
    @Autowired
    private AccountService accountService;
    
    public PlaylistRes getPlaylists(SubsonicCommonReq req, String username) {
        username = StringUtils.isBlank(username) ? req.getU() : username;
        SysUserPojo user = accountService.getUser(username);
        List<TbCollectPojo> userPlayList = qukuService.getUserPlayList(user.getId(),
                Arrays.asList(PlayListTypeConfig.ORDINARY, PlayListTypeConfig.ORDINARY));
        
        ArrayList<Playlist> playlist = new ArrayList<>();
        for (TbCollectPojo collectPojo : userPlayList) {
            Playlist e = new Playlist();
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
        PlaylistRes playlistRes = new PlaylistRes();
        playlistRes.setPlaylists(playlists);
        return playlistRes;
    }
}
