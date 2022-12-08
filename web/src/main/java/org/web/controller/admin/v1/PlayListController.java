package org.web.controller.admin.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.admin.model.dto.MusicDto;
import org.api.admin.service.PlayListApi;
import org.core.common.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("AdminPlayList")
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
    @GetMapping("/all")
    public R getAllMusicPage(@RequestBody MusicDto req) {
        return R.success(playList.getAllMusicPage(req));
    }
    
    /**
     * 获取歌单音乐
     *
     * @param playId 歌单ID
     * @param req    条件参数
     * @return 返回数据
     */
    @GetMapping("/{playId}")
    public R getPlaylist(@PathVariable("playId") String playId, @RequestBody MusicDto req) {
        return R.success(playList.getPlaylist(playId, req));
    }
}
