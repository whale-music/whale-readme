package org.web.controller.admin.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.admin.dto.MusicDto;
import org.core.common.result.R;
import org.springframework.web.bind.annotation.*;

@RestController("AdminPlayList")
@RequestMapping("/admin/playlist")
@Slf4j
public class PlayListController {
    
    /**
     * 获取全部音乐
     *
     * @param req 条件参数
     * @return 返回数据
     */
    @GetMapping("/all")
    public R getAllMusicPage(@RequestBody MusicDto req) {
        
        return R.success();
    }
    
    /**
     * 获取全部音乐
     *
     * @param playId 歌单ID
     * @param req    条件参数
     * @return 返回数据
     */
    @GetMapping("/{playId}")
    public R getPlaylist(@PathVariable("playId") String playId, @RequestBody MusicDto req) {
        
        return R.success();
    }
}
