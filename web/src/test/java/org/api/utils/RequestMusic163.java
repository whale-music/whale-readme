package org.api.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.cglib.beans.BeanMap;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    public static List<Long> like(String playId, String cookie) {
        String request = req(host + "/likelist?uid=" + playId, cookie);
        List<Long> read = JsonPath.read(request, "$.ids");
        return CollUtil.isEmpty(read) ? Collections.emptyList() : read;
    }
    
    /**
     * 歌单详情（获取歌单内所有数据）
     *
     * @param playId 歌单名
     */
    public static List<Long> getPlayDetail(String playId, String cookie) {
        String request = req(host + "/playlist/detail?id=" + playId, cookie);
        List<Long> read = null;
        try {
            read = JsonPath.read(request, "$.playlist.trackIds[*].id");
        } catch (PathNotFoundException e) {
            log.warn("歌曲详情: {}", request);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return CollUtil.isEmpty(read) ? Collections.emptyList() : read;
    }
    
    /**
     * 获取歌曲详情
     */
    public static List<Map<String, Object>> getSongDetail(List<Long> musicIds, String cookie) {
        String request = req(host + "/song/detail?ids=" + ArrayUtil.join(musicIds.toArray(), ","), cookie);
        List<Map<String, Object>> list = null;
        try {
            list = JsonPath.read(request, "$.songs");
        }catch (PathNotFoundException e) {
            log.warn("歌曲详情: {}", request);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return CollUtil.isEmpty(list) ? Collections.emptyList() : list;
    }
    
    /**
     * 获取专辑信息
     */
    public static Map<String, Object> getAlbumDto(Integer albumId, String cookie) {
        String request = req(host + "/album?id=" + albumId, cookie);
        Map<String, Object> map = null;
        try {
            map = JsonPath.read(request, "$.album");
        } catch (PathNotFoundException e) {
            log.warn("无专辑信息: {}", request);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return map != null ? map : new HashMap<>();
    }
    
    /**
     * 获取歌曲下载地址
     */
    public static List<Map<String,Object>> getSongUrl(List<Long> musicIds, String cookie, int flag) {
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
    public static List<Map<String,Object>> getSongUrlV1(List<Long> musicIds, String cookie) {
        String request = req(host + "/song/url?id=" + ArrayUtil.join(musicIds.toArray(), ","), cookie);
        List<Map<String,Object>> map = JsonPath.read(request, "$.data");
        return CollUtil.isEmpty(map) ? Collections.emptyList() : map;
    }
    
    /**
     * 获取歌曲下载地址
     */
    public static List<Map<String,Object>> getSongUrlV2(List<Long> musicIds, String cookie) {
        String request = req(host + "/song/url/v1?id=" + ArrayUtil.join(musicIds.toArray(), ",") + "&level=hires", cookie);
        List<Map<String,Object>> map = JsonPath.read(request, "$.data");
        return CollUtil.isEmpty(map) ? Collections.emptyList() : map;
    }
    
    /**
     * 获取歌词
     */
    public static Map<String, String> getLyric(Long musicId, String cookie) {
        String request = req(host + "/lyric?id=" + musicId, cookie);
        BeanMap beanMap = BeanMap.create(request);
        BeanMap lrcMap = MapUtil.get(beanMap, "lrc", BeanMap.class);
        String lyric = MapUtil.getStr(lrcMap, "lyric");
        
        BeanMap klyricMap = MapUtil.get(beanMap, "klyric", BeanMap.class);
        String klyric = MapUtil.getStr(klyricMap, "lyric");
    
        // String lrc = JsonPath.read(request, "$.lrc.lyric");
        // String klyric = JsonPath.read(request, "$.klyric.lyric");
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("lrc", lyric);
        stringStringHashMap.put("klyric", klyric);
        return stringStringHashMap;
    }
    
    /**
     * 获取歌曲作者信息
     */
    public static Map<String, Object> getSingerInfo(Long singerId, String cookie) {
        String request = req(host + "/artist/detail?id=" + singerId, cookie);
        Map<String, Object> map = null;
        try {
            map = JsonPath.read(request, "$.data");
        } catch (PathNotFoundException e) {
            log.warn("无作者信息: {}", request);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return map != null ? map : new HashMap<>();
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
