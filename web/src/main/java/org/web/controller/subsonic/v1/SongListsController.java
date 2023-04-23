package org.web.controller.subsonic.v1;


import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.ManualSerialize;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.req.album.albumlist2.AlbumReq;
import org.api.subsonic.model.res.album.albumlist2.AlbumRes;
import org.api.subsonic.service.SongListsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(SubsonicConfig.SUBSONIC + "SongListsController")
@RequestMapping("/rest")
@Slf4j
@CrossOrigin(origins = "*")
public class SongListsController {
    @Autowired
    private SongListsApi songListsApi;
    
    
    @GetMapping("/getAlbumList2.view")
    @ManualSerialize
    public Object getAlbumList2(SubsonicCommonReq req, AlbumReq albumReq) {
        AlbumRes res = songListsApi.getAlbumList2(req, albumReq);
        return res.success();
    }
}
