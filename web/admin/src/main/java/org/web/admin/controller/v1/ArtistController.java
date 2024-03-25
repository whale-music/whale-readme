package org.web.admin.controller.v1;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.common.PageResCommon;
import org.api.admin.model.req.AlbumListPageReq;
import org.api.admin.model.req.ArtistPageReq;
import org.api.admin.model.req.RemoveArtistReq;
import org.api.admin.model.req.SaveOrUpdateArtistReq;
import org.api.admin.model.res.ArtistInfoRes;
import org.api.admin.model.res.ArtistMvListRes;
import org.api.admin.model.res.ArtistPageRes;
import org.api.admin.model.res.ArtistRes;
import org.api.admin.service.ArtistApi;
import org.core.common.result.R;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.core.mybatis.model.convert.ArtistConvert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController(AdminConfig.ADMIN + "ArtistController")
@RequestMapping("/admin/singer")
@Slf4j
public class ArtistController {
    
    
    private final ArtistApi artistApi;
    
    public ArtistController(ArtistApi artistApi) {
        this.artistApi = artistApi;
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/allSinger")
    public R getAllSingerList(@RequestBody AlbumListPageReq req) {
        Page<ArtistRes> page = artistApi.getAllSingerList(req);
        return R.success(page);
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/select")
    public R getSelectedSinger(@RequestParam(value = "name", required = false) String name) {
        List<Map<String, Object>> maps = artistApi.getSelectedSinger(name);
        return R.success(maps);
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/getArtistByAlbumId")
    public R getArtistListByAlbumId(@RequestParam(value = "id") Long albumId) {
        List<ArtistConvert> byAlbumId = artistApi.getSingerListByAlbumId(albumId);
        return R.success(byAlbumId);
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/{id}")
    public R getArtistById(@PathVariable("id") Long id) {
        ArtistInfoRes artist = artistApi.getArtistById(id);
        return R.success(artist);
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @DeleteMapping("/")
    public R deleteArtist(@RequestBody RemoveArtistReq req) {
        artistApi.deleteArtist(req.getIds());
        return R.success();
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/")
    public R saveOrUpdateArtist(@RequestBody SaveOrUpdateArtistReq req) {
        artistApi.saveOrUpdateArtist(req);
        return R.success();
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/mv")
    public R getMvList(@RequestParam("id") Long id) {
        List<ArtistMvListRes> mvList = artistApi.getMvList(id);
        return R.success(mvList);
    }
    
    
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/page")
    public R getArtistPage(@RequestBody ArtistPageReq req) {
        PageResCommon<ArtistPageRes> res = artistApi.getArtistPage(req);
        return R.success(res);
    }
}
