package org.web.controller.subsonic.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.ManualSerialize;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.playlists.PlaylistRes;
import org.api.subsonic.service.PlaylistApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        PlaylistRes playlists = playlistApi.getPlaylists(req, username);
        return playlists.success();
    }
}
