package org.web.controller.subsonic.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.ManualSerialize;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.playlist.PlaylistRes;
import org.api.subsonic.model.res.playlists.PlaylistsRes;
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
    
    @GetMapping("/getPlaylists.view")
    @ManualSerialize
    public Object getPlaylists(SubsonicCommonReq req, String username) {
        PlaylistsRes playlists = playlistApi.getPlaylists(req, username);
        return playlists.success();
    }
    
    @GetMapping("/getPlaylist.view")
    @ManualSerialize
    public Object getPlaylist(SubsonicCommonReq req,@RequestParam("id") Long id) {
        PlaylistRes playlists = playlistApi.getPlaylist(req, id);
        return playlists.success();
    }
}
