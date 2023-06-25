package org.web.controller.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.convert.Count;
import org.api.admin.service.HoneApi;
import org.core.common.result.R;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController(AdminConfig.ADMIN + "HomeController")
@RequestMapping("/admin/home")
@Slf4j
public class HomeController {
    
    @Autowired
    private HoneApi honeApi;
    
    @GetMapping("/count")
    @Operation(summary = "获取数据库统计")
    public R getCount() {
        Count musicCount = honeApi.getMusicCount();
        Count albumCount = honeApi.getAlbumCount();
        Count artistCount = honeApi.getArtistCount();
        HashMap<String, Count> map = new HashMap<>();
        map.put("music", musicCount);
        map.put("album", albumCount);
        map.put("artist", artistCount);
        return R.success(map);
    }
    
    @GetMapping("/musicTop")
    public R getMusicTop() {
        List<MusicConvert> hone = honeApi.getMusicTop();
        return R.success(hone);
    }
    
    @GetMapping("/albumTop")
    public R getAlbumTop() {
        List<AlbumConvert> hone = honeApi.getAlbumTop();
        return R.success(hone);
    }
    
    @GetMapping("/artistTop")
    public R getArtist() {
        List<ArtistConvert> hone = honeApi.getArtist();
        return R.success(hone);
    }
    
    @GetMapping("/music/statistics")
    public R getMusicStatistics() {
        return R.success(honeApi.getMusicStatistics());
    }
    
    @GetMapping("/music/task")
    public R getPluginTask(@RequestParam(value = "id", required = false) Long id) {
        id = Optional.ofNullable(id).orElse(UserUtil.getUser().getId());
        return R.success(honeApi.getPluginTask(id));
    }
}
