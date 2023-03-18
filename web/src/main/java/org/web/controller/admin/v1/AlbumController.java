package org.web.controller.admin.v1;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.AlbumReq;
import org.api.admin.model.res.AlbumRes;
import org.api.admin.service.AlbumApi;
import org.core.common.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
