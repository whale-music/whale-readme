package org.web.admin.controller.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.res.NewAlbumRes;
import org.api.admin.model.res.NewArtistRes;
import org.api.admin.model.res.NewMusicRes;
import org.api.admin.service.RecommendApi;
import org.core.common.result.R;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController(AdminConfig.ADMIN + "RecommendController")
@RequestMapping("/admin/recommend")
public class RecommendController {
    private final RecommendApi recommendApi;
    
    public RecommendController(RecommendApi recommendApi) {
        this.recommendApi = recommendApi;
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/new/musics")
    public R getNewMusic(@RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {
        List<NewMusicRes> res = recommendApi.getNewMusic(count);
        return R.success(res);
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/new/artists")
    public R getNewArtist(@RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {
        List<NewArtistRes> res = recommendApi.getNewArtist(count);
        return R.success(res);
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/new/albums")
    public R getNewAlbums(@RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {
        List<NewAlbumRes> res = recommendApi.getNewAlbums(count);
        return R.success(res);
    }
}
