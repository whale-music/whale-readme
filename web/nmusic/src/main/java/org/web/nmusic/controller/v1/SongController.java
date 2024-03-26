package org.web.nmusic.controller.v1;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.nmusic.config.NeteaseCloudConfig;
import org.api.nmusic.model.vo.song.lyric.SongLyricRes;
import org.api.nmusic.model.vo.songdetail.SongDetailRes;
import org.api.nmusic.model.vo.songurl.SongUrlRes;
import org.api.nmusic.service.MusicApi;
import org.core.common.annotation.AnonymousAccess;
import org.core.common.result.NeteaseResult;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * NMusicApi 歌曲控制器
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@RestController(NeteaseCloudConfig.NETEASECLOUD + "SongController")
@RequestMapping("/")
@Slf4j
public class SongController {
    
    private final MusicApi musicApi;
    
    public SongController(MusicApi musicApi) {
        this.musicApi = musicApi;
    }
    
    /**
     * 获取歌曲详情
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/song/detail", method = {RequestMethod.GET, RequestMethod.POST})
    @AnonymousAccess
    public NeteaseResult songDetail(@RequestParam("ids") List<Long> ids) {
        SongDetailRes res = musicApi.songDetail(ids);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    
    
    /**
     * 获取歌曲下载地址
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/song/url", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult songUrl(@RequestParam("id") List<Long> id, @RequestParam(value = "br", required = false, defaultValue = "999000") Integer br) {
        SongUrlRes songUrlRes = musicApi.songUrl(id, br);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(songUrlRes));
        return r.success();
    }
    
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/lyric", method = {RequestMethod.GET, RequestMethod.POST})
    @AnonymousAccess
    public NeteaseResult lyric(@RequestParam("id") Long id) {
        SongLyricRes res = musicApi.lyric(id);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/scrobble", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult scrobble(@RequestParam("id") Long id, @RequestParam("sourceid") Long sourceid, @RequestParam(value = "time", required = false) Long time) {
        musicApi.scrobble(id, sourceid, time);
        NeteaseResult r = new NeteaseResult();
        return r.success();
    }
}
