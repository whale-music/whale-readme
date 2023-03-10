package org.web.controller.admin.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.admin.model.req.AlbumReq;
import org.core.common.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("Album")
@RequestMapping("/admin/album")
@Slf4j
public class AlbumController {
    
    @GetMapping("/admin/allalbum")
    public R getAllAlbums(@RequestBody AlbumReq req) {
        return R.success();
    }
}
