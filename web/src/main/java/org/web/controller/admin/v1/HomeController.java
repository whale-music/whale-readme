package org.web.controller.admin.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.service.HoneApi;
import org.core.common.result.R;
import org.core.pojo.TbAlbumPojo;
import org.core.pojo.TbMusicPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(AdminConfig.ADMIN + "HomeController")
@RequestMapping("/admin/home")
@Slf4j
public class HomeController {
    
    @Autowired
    private HoneApi honeApi;
    
    @GetMapping("/music/count")
    public R getMusicCount() {
        return R.success(honeApi.getMusicCount());
    }
    
    @GetMapping("/album/count")
    public R getAlbumCount() {
        return R.success(honeApi.getAlbumCount());
    }
    
    @GetMapping("/artist/count")
    public R getArtistCount() {
        return R.success(honeApi.getArtistCount());
    }
    
    @GetMapping("/musicTop")
    public R getMusicTop() {
        List<TbMusicPojo> hone = honeApi.getMusicTop();
        return R.success(hone);
    }
    
    @GetMapping("/albumTop")
    public R getAlbumTop() {
        List<TbAlbumPojo> hone = honeApi.getAlbumTop();
        return R.success(hone);
    }
    
    @GetMapping("/music/statistics")
    public R getMusicStatistics() {
        return R.success(honeApi.getMusicStatistics());
    }
}
