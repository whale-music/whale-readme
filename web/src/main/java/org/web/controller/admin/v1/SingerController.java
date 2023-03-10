package org.web.controller.admin.v1;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.model.req.AlbumReq;
import org.api.admin.model.res.SingerRes;
import org.api.admin.service.SingerApi;
import org.core.common.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("Singer")
@RequestMapping("/admin/singer")
@Slf4j
public class SingerController {
    
    
    @Autowired
    private SingerApi singerApi;
    
    @PostMapping("/allSinger")
    public R getAllSingerList(@RequestBody AlbumReq req) {
        Page<SingerRes> page = singerApi.getAllSingerList(req);
        return R.success(page);
    }
}
