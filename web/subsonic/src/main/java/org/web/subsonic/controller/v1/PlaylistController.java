package org.web.subsonic.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.ManualSerialize;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.playlist.PlaylistRes;
import org.api.subsonic.model.res.playlists.PlaylistsRes;
import org.api.subsonic.model.res.starred2.Starred2Res;
import org.api.subsonic.service.PlaylistApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController(SubsonicConfig.SUBSONIC + "PlaylistController")
@RequestMapping("/rest")
@Slf4j
@CrossOrigin(origins = "*")
public class PlaylistController {
    
    @Autowired
    private PlaylistApi playlistApi;
    
    @GetMapping({"/getPlaylists.view", "/getPlaylists"})
    @ManualSerialize
    public Object getPlaylists(SubsonicCommonReq req, String username) {
        PlaylistsRes playlists = playlistApi.getPlaylists(req, username);
        return playlists.success();
    }
    
    @Operation(summary = "获取用户收藏",
               description = "返回用户喜爱歌单"
    )
    @GetMapping(value = "/getStarred2")
    @ManualSerialize
    public Object getStarred2(SubsonicCommonReq req) {
        Starred2Res starred2 = playlistApi.getStarred2(req);
        return starred2.success();
    }
    
    @GetMapping({"/getPlaylist.view", "/getPlaylist"})
    @ManualSerialize
    public Object getPlaylist(SubsonicCommonReq req, @RequestParam("id") Long id) {
        PlaylistRes playlists = playlistApi.getPlaylist(id);
        return playlists.success();
    }
}
