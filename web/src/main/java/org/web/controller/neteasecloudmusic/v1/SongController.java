package org.web.controller.neteasecloudmusic.v1;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.model.vo.song.lyric.SongLyricRes;
import org.api.neteasecloudmusic.model.vo.songdetail.SongDetailRes;
import org.api.neteasecloudmusic.model.vo.songurl.SongUrlRes;
import org.api.neteasecloudmusic.service.MusicApi;
import org.core.common.result.NeteaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * NeteaseCloudMusicApi 歌曲控制器
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@RestController("NeteaseCloudSong")
@RequestMapping("/")
@Slf4j
public class SongController {
    
    @Autowired
    private MusicApi musicApi;
    
    /**
     * 获取歌曲详情
     */
    @RequestMapping(value = "/song/detail", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult songDetail(@RequestParam("ids") List<Long> ids) {
        SongDetailRes res = musicApi.songDetail(ids);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    
    
    /**
     * 获取歌曲下载地址
     */
    @RequestMapping(value = "/song/url", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult songUrl(@RequestParam("id") List<Long> id, @RequestParam(value = "br", required = false, defaultValue = "999000") Integer br) {
        SongUrlRes songUrlRes = musicApi.songUrl(id, br);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(songUrlRes));
        return r.success();
    }
    
    @RequestMapping(value = "/lyric", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult lyric(@RequestParam("id") Long id) {
        SongLyricRes res = musicApi.lyric(id);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    
    @GetMapping("/scrobble")
    public NeteaseResult scrobble(@RequestParam("id") Long id, @RequestParam("sourceid") Long sourceid, @RequestParam(value = "time", required = false) Integer time) {
        musicApi.scrobble(id,sourceid,time);
        NeteaseResult r = new NeteaseResult();
        return r.success();
    }
}
