package org.web.admin.controller.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.LinkAudioResourceReq;
import org.api.admin.model.req.LinkPicResourceReq;
import org.api.admin.model.req.LinkVideoResourceReq;
import org.api.admin.model.req.ResourcePageReq;
import org.api.admin.model.res.*;
import org.api.admin.service.ResourceApi;
import org.core.common.result.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    
    @PostMapping("/list")
    public R getResourcePage(@RequestBody ResourcePageReq req) {
        List<ResourcePageRes> res = resourceApi.getResourcePage(req);
        return R.success(res);
    }
    
    @GetMapping("/filter")
    public R getFilterType(@RequestParam("type") String type) {
        Collection<FilterTermsRes> types = resourceApi.getFilterType(type);
        return R.success(types);
    }
    
    @GetMapping("/audio")
    public R getAudioResourceInfo(@RequestParam("path") String path) {
        ResourceAudioInfoRes fileInfo = resourceApi.getAudioResourceInfo(path);
        return R.success(fileInfo);
    }
    
    @GetMapping("/pic")
    public R getPicResourceInfo(@RequestParam("path") String path) {
        ResourcePicInfoRes res = resourceApi.getPicResourceInfo(path);
        return R.success(res);
    }
    
    @GetMapping("/video")
    public R getVideoResourceInfo(@RequestParam("path") String path) {
        ResourceVideoInfoRes res = resourceApi.getVideoResourceInfo(path);
        return R.success(res);
    }
    
    @GetMapping("/auto/music")
    public R getMusicAutocomplete(@RequestParam("name") String name) {
        List<AutocompleteMusicRes> res = resourceApi.getMusicAutocomplete(name);
        return R.success(res);
    }
    
    @GetMapping("/auto/mv")
    public R getMvAutocomplete(@RequestParam("name") String name) {
        List<AutocompleteMvRes> res = resourceApi.getMvAutocomplete(name);
        return R.success(res);
    }
    
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
    @PostMapping("/link/pic")
    public R linkPicture(@RequestBody @Validated LinkPicResourceReq picResource) {
        resourceApi.linkPicture(picResource);
        return R.success();
    }
    
    @PostMapping("/link/video")
    public R linkVideo(@RequestBody LinkVideoResourceReq videoResourceReq) {
        resourceApi.linkVideo(videoResourceReq);
        return R.success();
    }
    
    @PostMapping("/link/audio")
    public R linkAudio(@RequestBody LinkAudioResourceReq audioResourceReq) {
        resourceApi.linkAudio(audioResourceReq);
        return R.success();
    }
    
}
