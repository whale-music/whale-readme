package org.web.controller.subsonic.v1;


import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.ManualSerialize;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.req.albumlist2.AlbumReq;
import org.api.subsonic.model.res.albumlist2.AlbumList2Res;
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
        albumReq.setSize(albumReq.getSize() == null || albumReq.getSize() == 0 ? 20 : albumReq.getSize());
        albumReq.setOffset(albumReq.getOffset() == null ? 0 : albumReq.getOffset() / albumReq.getSize());
        AlbumList2Res res = songListsApi.getAlbumList2(albumReq);
        return res.success();
    }
}
