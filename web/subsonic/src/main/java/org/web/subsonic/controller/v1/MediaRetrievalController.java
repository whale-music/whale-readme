package org.web.subsonic.controller.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.service.MediaRetrievalApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController(SubsonicConfig.SUBSONIC + "MediaRetrievalController")
@RequestMapping("/rest")
@Slf4j
@CrossOrigin(origins = "*")
public class MediaRetrievalController {
    
    @Autowired
    private MediaRetrievalApi mediaRetrievalApi;
    
    @GetMapping({"/getCoverArt.view", "/getCoverArt"})
    public RedirectView getCoverArt(SubsonicCommonReq req, @RequestParam(value = "id") Long id) {
        String picUrl = mediaRetrievalApi.getCoverArt(req, id);
        return new RedirectView(picUrl);
    }
    
    @GetMapping({"/stream.view", "/stream"})
    public RedirectView stream(SubsonicCommonReq req, @RequestParam(value = "id") Long id) {
        String musicUrl = mediaRetrievalApi.stream(req, id);
        return new RedirectView(musicUrl);
    }
}
