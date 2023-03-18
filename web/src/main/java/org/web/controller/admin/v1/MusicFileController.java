package org.web.controller.admin.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.service.MusicFileApi;
import org.core.common.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController(AdminConfig.ADMIN + "MusicFileController")
@RequestMapping("/admin/music")
@Slf4j
public class MusicFileController {
    @Autowired
    private MusicFileApi musicFileApi;
    
    @GetMapping("/get/{musicId}")
    public R getMusic(@PathVariable("musicId") Set<String> musicId) {
        return R.success(musicFileApi.getMusic(musicId));
    }
}
