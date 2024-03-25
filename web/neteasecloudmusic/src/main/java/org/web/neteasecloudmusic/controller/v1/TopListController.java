package org.web.neteasecloudmusic.controller.v1;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.toplist.artist.TopListArtistRes;
import org.api.neteasecloudmusic.model.vo.toplist.playlist.TopListPlayListRes;
import org.api.neteasecloudmusic.model.vo.toplist.toplist.TopListRes;
import org.api.neteasecloudmusic.service.TopListApi;
import org.core.common.annotation.AnonymousAccess;
import org.core.common.result.NeteaseResult;
import org.core.common.weblog.annotation.WebLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(NeteaseCloudConfig.NETEASECLOUD + "TopListController")
@RequestMapping("/")
@Slf4j
public class TopListController {
    private final TopListApi topListApi;
    
    public TopListController(TopListApi topListApi) {
        this.topListApi = topListApi;
    }
    
    @WebLog
    @GetMapping("/toplist")
    public NeteaseResult toplist() {
        TopListRes res = topListApi.toplist();
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    
    @WebLog
    @GetMapping("/top/playlist")
    public NeteaseResult topPlaylist(@RequestParam(value = "order", required = false, defaultValue = "hot") String order, String cat, @RequestParam(value = "limit", required = false, defaultValue = "50") String limit, @RequestParam(value = "offset", required = false, defaultValue = "0") String offset) {
        TopListPlayListRes res = topListApi.topPlaylist(order, cat, Long.valueOf(offset), Long.valueOf(limit));
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    
    @WebLog
    @GetMapping("/toplist/artist")
    @AnonymousAccess
    public NeteaseResult artist(String type) {
        TopListArtistRes res = topListApi.artist(type);
        NeteaseResult r = new NeteaseResult();
        r.put("list",res);
        return r.success();
    }
}
