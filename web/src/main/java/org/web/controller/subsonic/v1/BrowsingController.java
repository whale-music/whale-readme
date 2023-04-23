package org.web.controller.subsonic.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.ManualSerialize;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.album.AlbumRes;
import org.api.subsonic.model.res.song.SongRes;
import org.api.subsonic.service.BrowsingApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController(SubsonicConfig.SUBSONIC + "BrowsingController")
@RequestMapping("/rest")
@Slf4j
@CrossOrigin(origins = "*")
public class BrowsingController {
    
    @Autowired
    private BrowsingApi browsingApi;
    
    @GetMapping("/getAlbum.view")
    @ManualSerialize
    public Object getAlbum(SubsonicCommonReq req, @RequestParam("id") Long id) {
        AlbumRes res = browsingApi.getAlbum(id);
        return res.success();
    }
    
    @GetMapping("/getSong.view")
    @ManualSerialize
    public Object getSong(SubsonicCommonReq req, @RequestParam("id") Long id) {
        SongRes res = browsingApi.getSong(id);
        return res.success();
    }
}
