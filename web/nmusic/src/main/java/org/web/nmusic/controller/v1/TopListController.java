package org.web.nmusic.controller.v1;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.nmusic.config.NeteaseCloudConfig;
import org.api.nmusic.model.vo.toplist.artist.TopListArtistRes;
import org.api.nmusic.model.vo.toplist.playlist.TopListPlayListRes;
import org.api.nmusic.model.vo.toplist.toplist.TopListRes;
import org.api.nmusic.service.TopListApi;
import org.core.common.annotation.AnonymousAccess;
import org.core.common.result.NeteaseResult;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/toplist", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult toplist() {
        TopListRes res = topListApi.toplist();
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/top/playlist", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult topPlaylist(@RequestParam(value = "order", required = false, defaultValue = "hot") String order, String cat, @RequestParam(value = "limit", required = false, defaultValue = "50") String limit, @RequestParam(value = "offset", required = false, defaultValue = "0") String offset) {
        TopListPlayListRes res = topListApi.topPlaylist(order, cat, Long.valueOf(offset), Long.valueOf(limit));
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/toplist/artist", method = {RequestMethod.GET, RequestMethod.POST})
    @AnonymousAccess
    public NeteaseResult artist(String type) {
        TopListArtistRes res = topListApi.artist(type);
        NeteaseResult r = new NeteaseResult();
        r.put("list",res);
        return r.success();
    }
}
