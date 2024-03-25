package org.web.admin.controller.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.*;
import org.api.admin.model.res.*;
import org.api.admin.service.ResourceApi;
import org.core.common.result.R;
import org.core.common.weblog.annotation.WebLog;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController(AdminConfig.ADMIN + "ResourceController")
@RequestMapping("/admin/resource")
@Slf4j
public class ResourceController {
    
    private final ResourceApi resourceApi;
    
    public ResourceController(ResourceApi resourceApi) {
        this.resourceApi = resourceApi;
    }
    
    @WebLog
    @PostMapping("/list")
    public R getResourcePage(@RequestBody ResourcePageReq req) {
        List<ResourcePageRes> res = resourceApi.getResourcePage(req);
        return R.success(res);
    }
    
    @WebLog
    @GetMapping("/filter")
    public R getFilterType(@RequestParam("type") String type) {
        Collection<FilterTermsRes> types = resourceApi.getFilterType(type);
        return R.success(types);
    }
    
    @WebLog
    @GetMapping("/audio")
    public R getAudioResourceInfo(@RequestParam("path") String path) {
        ResourceAudioInfoRes fileInfo = resourceApi.getAudioResourceInfo(path);
        return R.success(fileInfo);
    }
    
    @WebLog
    @GetMapping("/pic")
    public R getPicResourceInfo(@RequestParam("path") String path) {
        ResourcePicInfoRes res = resourceApi.getPicResourceInfo(path);
        return R.success(res);
    }
    
    @WebLog
    @GetMapping("/video")
    public R getVideoResourceInfo(@RequestParam("path") String path) {
        ResourceVideoInfoRes res = resourceApi.getVideoResourceInfo(path);
        return R.success(res);
    }
    
    @WebLog
    @GetMapping("/auto/music")
    public R getMusicAutocomplete(@RequestParam("name") String name) {
        List<AutocompleteMusicRes> res = resourceApi.getMusicAutocomplete(name);
        return R.success(res);
    }
    
    @WebLog
    @GetMapping("/auto/mv")
    public R getMvAutocomplete(@RequestParam("name") String name) {
        List<AutocompleteMvRes> res = resourceApi.getMvAutocomplete(name);
        return R.success(res);
    }
    
    @WebLog
    @GetMapping("/auto/pic")
    public R getPicAutocomplete(@RequestParam("name") String name, @RequestParam("type") String type) {
        List<AutocompletePicRes> res = resourceApi.getPicAutocomplete(name, type);
        return R.success(res);
    }
    
    /**
     * 关联封面
     *
     * @param picResource 需要关联数据
     * @return 返回数据
     */
    @WebLog
    @PostMapping("/link/pic")
    public R linkPicture(@RequestBody @Validated LinkPicResourceReq picResource) {
        resourceApi.linkPicture(picResource);
        return R.success();
    }
    
    @WebLog
    @PostMapping("/link/video")
    public R linkVideo(@RequestBody @Validated LinkVideoResourceReq videoResourceReq) {
        resourceApi.linkVideo(videoResourceReq);
        return R.success();
    }
    
    @WebLog
    @PostMapping("/link/audio")
    public R linkAudio(@RequestBody @Validated LinkAudioResourceReq audioResourceReq) {
        resourceApi.linkAudio(audioResourceReq);
        return R.success();
    }
    
    /**
     * 同步资源数据
     *
     * @param resource 需要关联数据
     * @return 返回数据
     */
    @WebLog
    @PostMapping("/sync/resource")
    public R syncResource(@RequestBody @Validated SyncResourceReq resource) throws IOException {
        resourceApi.syncResource(resource);
        return R.success();
    }
    
    /**
     * 清楚资源数据
     *
     * @param cleanResource 关联数据
     * @return 返回数据
     */
    @WebLog
    @PostMapping("/clean/resource")
    public R cleanResource(@RequestBody @Validated CleanResourceReq cleanResource) {
        resourceApi.cleanResource(cleanResource);
        return R.success();
    }
    
}
