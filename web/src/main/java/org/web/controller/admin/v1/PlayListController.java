package org.web.controller.admin.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.MusicPageReq;
import org.api.admin.model.res.router.RouterVo;
import org.api.admin.service.PlayListApi;
import org.core.common.result.R;
import org.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(AdminConfig.ADMIN + "PlayListController")
@RequestMapping("/admin/playlist")
@Slf4j
public class PlayListController {
    
    @Autowired
    private PlayListApi playList;
    
    /**
     * 获取全部音乐
     *
     * @param req 条件参数
     * @return 返回数据
     */
    @PostMapping("/page")
    public R getMusicPage(@RequestBody MusicPageReq req) {
        return R.success(playList.getMusicPage(req));
    }
    
    /**
     * 获取歌单音乐
     *
     * @param playId 歌单ID
     * @return 返回数据
     */
    @RequestMapping(value = "/{playId}", method = {RequestMethod.GET, RequestMethod.POST})
    public R getPlaylist(@PathVariable("playId") String playId) {
        return R.success(playList.getPlaylist(playId));
    }
    
    /**
     * 获取歌单动态路由（歌单数据）
     *
     * @param uid 用户ID
     * @return 动态路由歌单数据
     */
    @GetMapping("/getAsyncRoutes")
    public R getAsyncRoutes(@RequestParam(value = "uid", required = false) Long uid) {
        uid = uid == null ? UserUtil.getUser().getId() : uid;
        List<RouterVo> playListRouters = playList.getAsyncPlayListRoutes(uid);
        return R.success(playListRouters);
    }
}
