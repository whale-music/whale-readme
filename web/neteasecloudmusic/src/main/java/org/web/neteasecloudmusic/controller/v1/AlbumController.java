package org.web.neteasecloudmusic.controller.v1;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.album.album.AlbumRes;
import org.api.neteasecloudmusic.model.vo.album.detail.AlbumDetailRes;
import org.api.neteasecloudmusic.model.vo.album.sublist.AlbumSubListRes;
import org.api.neteasecloudmusic.service.AlbumApi;
import org.core.common.result.NeteaseResult;
import org.core.common.weblog.annotation.WebLog;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.utils.UserUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(NeteaseCloudConfig.NETEASECLOUD + "AlbumController")
@RequestMapping("/")
@Slf4j
public class AlbumController {
    
    private final AlbumApi albumApi;
    
    public AlbumController(AlbumApi albumApi) {
        this.albumApi = albumApi;
    }
    
    @WebLog
    @GetMapping("/album")
    public NeteaseResult album(@RequestParam("id") Long id) {
        AlbumRes res = albumApi.album(id);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    
    @WebLog
    @GetMapping("/album/sublist")
    public NeteaseResult albumSubList(@RequestParam(value = "limit", defaultValue = "25") Long limit, @RequestParam(value = "offset", defaultValue = "0") Long offset) {
        SysUserPojo user = UserUtil.getUser();
        AlbumSubListRes res = albumApi.albumSubList(user, limit, offset);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    
    @WebLog
    @GetMapping("/album/detail")
    public NeteaseResult albumDetail(@RequestParam("id") Long id) {
        AlbumDetailRes res = albumApi.albumDetail(id);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    

    
}
