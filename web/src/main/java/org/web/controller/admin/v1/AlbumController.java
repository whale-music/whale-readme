package org.web.controller.admin.v1;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.AlbumReq;
import org.api.admin.model.res.AlbumRes;
import org.api.admin.service.AlbumApi;
import org.core.common.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController(AdminConfig.ADMIN + "AlbumController")
@RequestMapping("/admin/album")
@Slf4j
public class AlbumController {
    
    @Autowired
    private AlbumApi albumApi;
    
    @PostMapping("/allAlbum")
    public R getAllAlbumList(@RequestBody AlbumReq req) {
        Page<AlbumRes> page = albumApi.getAllAlbumList(req);
        return R.success(page);
    }
    
    @GetMapping("/{albumId}")
    public R getAlbumInfo(@PathVariable Long albumId) {
        AlbumRes res = albumApi.getAlbumInfo(albumId);
        return R.success(res);
    }
    
    /**
     * 添加音乐时选择专辑接口
     *
     * @param name 专辑ID
     */
    @GetMapping("/select")
    public R getSelectAlbumList(@RequestParam(value = "name", required = false) String name) {
        List<Map<String, Object>> albums = albumApi.getSelectAlbumList(name);
        return R.success(albums);
    }
}
