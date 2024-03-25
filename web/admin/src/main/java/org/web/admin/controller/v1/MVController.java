package org.web.admin.controller.v1;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.RemoveMvReq;
import org.api.admin.model.req.SaveMvReq;
import org.api.admin.model.res.MvInfoRes;
import org.api.admin.model.res.MvPageRes;
import org.api.admin.service.MvApi;
import org.core.common.annotation.AnonymousAccess;
import org.core.common.result.R;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController(AdminConfig.ADMIN + "MVController")
@RequestMapping("/admin/mv")
public class MVController {
    
    private final MvApi mvApi;
    
    public MVController(MvApi mvApi) {
        this.mvApi = mvApi;
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/")
    public R getMvPage(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "artists", required = false) List<String> artists,
            @RequestParam(value = "tags", required = false) List<String> tags,
            @RequestParam(value = "order", required = false, defaultValue = "des") String order,
            @RequestParam(value = "orderBy", required = false, defaultValue = "createTime") String orderBy,
            @RequestParam(value = "index", defaultValue = "0", required = false) Long index,
            @RequestParam(value = "size", defaultValue = "10", required = false) Long size
    ) {
        Page<MvPageRes> mvPage = mvApi.getMvPage(title, artists, tags, order, orderBy, index, size);
        return R.success(mvPage);
    }
    
    @CrossOrigin
    @AnonymousAccess
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/upload/file")
    public R updateMvFile(@RequestParam(value = "file", required = false) MultipartFile uploadFile, @RequestParam("id") Long id) throws IOException {
        String url = mvApi.updateMvFile(uploadFile, id);
        return R.success(url);
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/{id}")
    public R getMvInfo(@PathVariable("id") Long id) {
        MvInfoRes res = mvApi.getMvInfo(id);
        return R.success(res);
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @DeleteMapping("/")
    public R removeMv(@RequestBody RemoveMvReq req) {
        mvApi.removeMv(req.getIds());
        return R.success();
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/save")
    public R saveMvInfo(@RequestBody SaveMvReq request) throws IOException {
        mvApi.saveMvInfo(request);
        return R.success();
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/update")
    public R updateMvInfo(@RequestBody SaveMvReq request) {
        mvApi.updateMvInfo(request);
        return R.success();
    }
}
