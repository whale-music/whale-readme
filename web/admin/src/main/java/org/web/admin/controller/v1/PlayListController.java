package org.web.admin.controller.v1;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.common.PageResCommon;
import org.api.admin.model.req.MusicPageReq;
import org.api.admin.model.req.PlayListReq;
import org.api.admin.model.req.PlaylistMusicPageReq;
import org.api.admin.model.req.UpdatePlayListReq;
import org.api.admin.model.res.PlayListAllRes;
import org.api.admin.model.res.PlayListRes;
import org.api.admin.model.res.PlaylistMusicPageRes;
import org.api.admin.model.res.router.RouterVo;
import org.api.admin.service.PlayListApi;
import org.core.common.result.R;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.core.utils.UserUtil;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController(AdminConfig.ADMIN + "PlayListController")
@RequestMapping("/admin/playlist")
@Slf4j
public class PlayListController {
    
    private final PlayListApi playList;
    
    public PlayListController(PlayListApi playList) {
        this.playList = playList;
    }
    
    /**
     * 获取全部音乐
     *
     * @param req 条件参数
     * @return 返回数据
     */
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/music/page")
    public R getMusicPage(@RequestBody MusicPageReq req) {
        return R.success(playList.getMusicPage(req));
    }
    
    /**
     * 移动端获取歌单音乐
     *
     * @param req 请求参数
     * @return 返回歌单音乐数据
     */
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/tracks/music/page")
    public R getPlaylistMusicPage(@RequestBody PlaylistMusicPageReq req) {
        PageResCommon<PlaylistMusicPageRes> res = playList.getPlaylistMusicPage(req);
        return R.success(res);
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/info/{id}")
    public R getPlayListInfo(@PathVariable("id") Long id) {
        return R.success(playList.getPlayListInfo(id));
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/info")
    public R updatePlayListInfo(@RequestBody UpdatePlayListReq req) {
        return R.success(playList.updatePlayListInfo(req));
    }
    
    /**
     * 获取歌单中音乐
     *
     * @param playId 歌单ID
     * @return 返回数据
     */
    @WebLog(LogNameConstant.ADMIN)
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
    @WebLog(LogNameConstant.ADMIN)
    @PutMapping(value = "/{name}")
    public R createPlayList(@PathVariable("name") String name) {
        return R.success(playList.createPlayList(name));
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @DeleteMapping("/{id}")
    public R deletePlayList(@PathVariable("id") List<Long> id) {
        playList.deletePlayList(UserUtil.getUser().getId(), id);
        return R.success();
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/user/{userId}")
    public R getUserPlayList(@PathVariable("userId") Long userId) {
        List<PlayListRes> collectPojoList = playList.getUserPlayList(userId);
        return R.success(collectPojoList);
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @RequestMapping(value = "/tracks", method = {RequestMethod.GET, RequestMethod.POST})
    public R addMusicToPlayList(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam("pid") Long pid,
            @RequestParam(value = "musicIds", required = false) List<Long> musicIds,
            @RequestBody(required = false) Map<String, List<Long>> mapMusicIds,
            @RequestParam(value = "flag", required = false, defaultValue = "true") Boolean flag
    ) {
        LinkedList<Long> ids = new LinkedList<>();
        if (CollUtil.isEmpty(musicIds)) {
            ids.addAll(mapMusicIds.get("musicIds"));
        } else {
            ids.addAll(musicIds);
        }
        playList.addMusicToPlayList(Optional.ofNullable(userId).orElse(UserUtil.getUser().getId()), pid, ids, flag);
        return R.success();
    }
    
    @WebLog(LogNameConstant.ADMIN)
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
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/getAsyncRoutes")
    public R getAsyncRoutes(@RequestParam(value = "uid", required = false) Long uid) {
        uid = uid == null ? UserUtil.getUser().getId() : uid;
        List<RouterVo> playListRouters = playList.getAsyncPlayListRoutes(uid);
        return R.success(playListRouters);
    }
    
    /**
     * 获取用户全部歌单
     *
     * @param uid 用户ID
     * @return 用户歌单
     */
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/user/all")
    public R getUserPlaylistAll(@RequestParam("id") Long uid) {
        List<PlayListAllRes> playListPage = playList.getUserPlaylistAll(uid);
        return R.success(playListPage);
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/page")
    public R getPlayListPage(@RequestBody PlayListReq req) {
        Page<PlayListRes> playListPage = playList.getPlayListPage(req);
        return R.success(playListPage);
    }
}
