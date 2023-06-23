package org.web.controller.admin.v1;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.AlbumReq;
import org.api.admin.model.req.SaveOrUpdateArtistReq;
import org.api.admin.model.res.ArtistInfoRes;
import org.api.admin.model.res.ArtistRes;
import org.api.admin.service.ArtistApi;
import org.core.common.result.R;
import org.core.mybatis.model.convert.ArtistConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController(AdminConfig.ADMIN + "ArtistController")
@RequestMapping("/admin/singer")
@Slf4j
public class ArtistController {
    
    
    @Autowired
    private ArtistApi artistApi;
    
    @PostMapping("/allSinger")
    public R getAllSingerList(@RequestBody AlbumReq req) {
        Page<ArtistRes> page = artistApi.getAllSingerList(req);
        return R.success(page);
    }
    
    @GetMapping("/select")
    public R getSelectedSinger(@RequestParam(value = "name", required = false) String name) {
        List<Map<String, Object>> maps = artistApi.getSelectedSinger(name);
        return R.success(maps);
    }
    
    @GetMapping("/getArtistByAlbumId")
    public R getArtistListByAlbumId(@RequestParam(value = "id") Long albumId) {
        List<ArtistConvert> byAlbumId = artistApi.getSingerListByAlbumId(albumId);
        return R.success(byAlbumId);
    }
    
    @GetMapping("/{id}")
    public R getArtistById(@PathVariable("id") Long id) {
        ArtistInfoRes artist = artistApi.getArtistById(id);
        return R.success(artist);
    }
    
    @DeleteMapping("/{id}")
    public R deleteArtist(@PathVariable("id") List<Long> id) {
        artistApi.deleteArtist(id);
        return R.success();
    }
    
    @PostMapping("/")
    public R saveOrUpdateArtist(@RequestBody SaveOrUpdateArtistReq req) {
        artistApi.saveOrUpdateArtist(req);
        return R.success();
    }
}
