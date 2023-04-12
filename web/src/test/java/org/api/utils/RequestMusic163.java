package org.api.utils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson2.JSON;
import org.api.model.LikePlay;
import org.api.model.album.AlbumRes;
import org.api.model.lyric.Lyric;
import org.api.model.playlist.PlayList;
import org.api.model.playlistdetail.PlayListDetailRes;
import org.api.model.singer.SingerRes;
import org.api.model.song.SongDetail;
import org.api.model.url.SongUrl;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class RequestMusic163 {
    private static final Log log = Log.get();
    public final static String host = "http://43.139.22.243:3000";
    
    private RequestMusic163() {
    }
    
    /**
     * 通用请求
     */
    @NotNull
    private static String req(String host, String cookie) {
        try (HttpResponse execute = HttpUtil.createGet(host).header("Cookie", cookie).execute()) {
            return execute.body();
        } catch (HttpException e) {
            throw new HttpException("http请求失败" + e);
        }
    }
    
    public static LikePlay like(String playId, String cookie) {
        String request = req(host + "/likelist?uid=" + playId, cookie);
        return JSON.parseObject(request, LikePlay.class);
    }
    
    /**
     * 歌单详情（获取歌单内所有数据）
     *
     * @param playId 歌单名
     */
    public static PlayListDetailRes getPlayDetail(String playId, String cookie) {
        String request = req(host + "/playlist/detail?id=" + playId, cookie);
        return JSON.parseObject(request, PlayListDetailRes.class);
    }
    
    public static PlayList getPlayList(String playId, String cookie) {
        String request = req(host + "/playlist/track/all?id=" + playId, cookie);
        return JSON.parseObject(request, PlayList.class);
    }
    
    /**
     * 获取歌曲详情
     */
    public static SongDetail getSongDetail(List<Long> musicIds, String cookie) {
        String request = req(host + "/song/detail?ids=" + ArrayUtil.join(musicIds.toArray(), ","), cookie);
        return JSON.parseObject(request, SongDetail.class);
    }
    
    /**
     * 获取专辑信息
     */
    public static AlbumRes getAlbumDto(Integer albumId, String cookie) {
        String request = req(host + "/album?id=" + albumId, cookie);
        return JSON.parseObject(request, AlbumRes.class);
    }
    
    /**
     * 获取歌曲下载地址
     */
    public static SongUrl getSongUrl(List<Long> musicIds, String cookie, int flag) {
        if (flag == 0) {
            return getSongUrlV1(musicIds, cookie);
        }
        if (flag == 1) {
            return getSongUrlV2(musicIds, cookie);
        }
        throw new RuntimeException();
    }
    
    /**
     * 获取歌曲下载地址
     */
    public static SongUrl getSongUrlV1(List<Long> musicIds, String cookie) {
        String request = req(host + "/song/url?id=" + ArrayUtil.join(musicIds.toArray(), ","), cookie);
        return JSON.parseObject(request, SongUrl.class);
    }
    
    /**
     * 获取歌曲下载地址
     */
    public static SongUrl getSongUrlV2(List<Long> musicIds, String cookie) {
        String request = req(host + "/song/url/v1?id=" + ArrayUtil.join(musicIds.toArray(), ",") + "&level=hires", cookie);
        return JSON.parseObject(request, SongUrl.class);
    }
    
    /**
     * 获取歌词
     */
    public static Lyric getLyric(Integer musicId, String cookie) {
        String request = req(host + "/lyric/url?id=" + musicId, cookie);
        return JSON.parseObject(request, Lyric.class);
    }
    
    /**
     * 获取歌曲作者信息
     */
    public static SingerRes getSingerInfo(int singerId, String cookie) {
        String request = req(host + "/artist/detail?id=" + singerId, cookie);
        return JSON.parseObject(request, SingerRes.class);
    }
    
    
    public static void download(String url, File saveFile) {
        try {
            HttpUtil.downloadFile(url, saveFile, 60000);
        } catch (Exception e) {
            log.info("重试一次{}", url);
            HttpUtil.downloadFile(url, saveFile, 60000);
        }
    }
}
