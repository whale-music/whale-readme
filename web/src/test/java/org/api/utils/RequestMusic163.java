package org.api.utils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson2.JSON;
import org.api.model.LikePlay;
import org.api.model.song.SongDetail;
import org.api.model.url.SongUrl;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class RequestMusic163 {
    private static final Log log = Log.get();
    public final static String host = "http://localhost:3000";
    
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
    
    public static LikePlay getPlayList(String playId, String cookie) {
        String request = req(host + "/playlist/track/all?id=" + playId, cookie);
        return JSON.parseObject(request, LikePlay.class);
    }
    
    
    public static SongDetail getSongDetail(List<Integer> musicIds, String cookie) {
        String request = req(host + "/song/detail?ids=" + ArrayUtil.join(musicIds.toArray(), ","), cookie);
        return JSON.parseObject(request, SongDetail.class);
    }
    
    public static SongUrl getNewSongUrl(List<Integer> musicIds, String cookie) {
        String request = req(host + "/song/url/v1?id=" + ArrayUtil.join(musicIds.toArray(), ",") + "&level=exhigh", cookie);
        return JSON.parseObject(request, SongUrl.class);
    }
    
    public static SongUrl getSongUrl(List<Integer> musicIds, String cookie) {
        String request = req(host + "/song/url?id=" + ArrayUtil.join(musicIds.toArray(), ","), cookie);
        return JSON.parseObject(request, SongUrl.class);
    }
    
    public static void download(String url, File saveFile) {
        try {
            HttpUtil.downloadFile(url, saveFile, 600);
        } catch (Exception e) {
            log.info("重试一次{}", url);
            HttpUtil.downloadFile(url, saveFile, 600);
        }
    }
}
