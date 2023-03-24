package org.web.controller.admin.v1;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.AlbumReq;
import org.api.admin.model.res.SingerRes;
import org.api.admin.service.SingerApi;
import org.core.common.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController(AdminConfig.ADMIN + "SingerController")
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
    
    @GetMapping("/select")
    public R getSelectedSinger(@RequestParam(value = "name", required = false) String name) {
        List<Map<String, Object>> maps = singerApi.getSelectedSinger(name);
        return R.success(maps);
    }
}
