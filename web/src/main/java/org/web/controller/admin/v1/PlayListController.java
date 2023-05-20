package org.web.controller.admin.v1;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.MusicPageReq;
import org.api.admin.model.req.PlayListReq;
import org.api.admin.model.res.PlayListRes;
import org.api.admin.model.res.router.RouterVo;
import org.api.admin.service.PlayListApi;
import org.core.common.result.R;
import org.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @PostMapping("/music/page")
    public R getMusicPage(@RequestBody MusicPageReq req) {
        return R.success(playList.getMusicPage(req));
    }
    
    @GetMapping("/info/{id}")
    public R getPlayListInfo(@PathVariable("id") Long id) {
        return R.success(playList.getPlayListInfo(id));
    }
    
    @PostMapping("/info")
    public R updatePlayListInfo(@RequestBody PlayListReq req) {
        return R.success(playList.updatePlayListInfo(req));
    }
    
    /**
     * 获取歌单中音乐
     *
     * @param playId 歌单ID
     * @return 返回数据
     */
    @RequestMapping(value = "/{playId}", method = {RequestMethod.GET, RequestMethod.POST})
    public R getAllMusic(@PathVariable("playId") String playId, @RequestBody(required = false) MusicPageReq page) {
        return R.success(playList.getAllMusic(playId, page));
    }
    
    /**
     * 创建歌单
     *
     * @param name 歌单名
     * @return 歌单创建信息
     */
    @PutMapping(value = "/{name}")
    public R createPlayList(@PathVariable("name") String name) {
        return R.success(playList.createPlayList(name));
    }
    
    @DeleteMapping("/{id}")
    public R deletePlayList(@PathVariable("id") List<Long> id) {
        playList.deletePlayList(UserUtil.getUser().getId(), id);
        return R.success();
    }
    
    @GetMapping("/user/{userId}")
    public R getUserPlayList(@PathVariable("userId") Long userId) {
        List<PlayListRes> collectPojoList = playList.getUserPlayList(userId);
        return R.success(collectPojoList);
    }
    
    @GetMapping("/tracks")
    public R addMusicToPlayList(@RequestParam(value = "userId", required = false) Long userId, @RequestParam("pid") Long pid, @RequestParam("musicIds") List<Long> musicIds, @RequestParam(value = "flag", required = false, defaultValue = "true") Boolean flag) {
        playList.addMusicToPlayList(Optional.ofNullable(userId).orElse(UserUtil.getUser().getId()), pid, musicIds, flag);
        return R.success();
    }
    
    @GetMapping("/like/{status}")
    public R like(@PathVariable("status") String status, @RequestParam("id") Long id) {
        playList.like(UserUtil.getUser().getId(), id, StringUtils.equals(status, "add"));
        return R.success();
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
    
    @PostMapping("/page")
    public R getPlayListPage(@RequestBody PlayListReq req) {
        Page<PlayListRes> playListPage = playList.getPlayListPage(req);
        return R.success(playListPage);
    }
}
