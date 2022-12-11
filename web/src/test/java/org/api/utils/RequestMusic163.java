package org.api.utils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import org.api.model.LikePlay;
import org.api.model.song.SongDetail;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RequestMusic163 {
    public final static String host = "http://localhost:3000";
    
    private RequestMusic163() {
    }
    
    public static LikePlay like(String playId, String cookie) {
        String request = getRequest(host + "/likelist?uid=" + playId, cookie);
        return JSON.parseObject(request, LikePlay.class);
    }
    
    @NotNull
    private static String getRequest(String host, String cookie) {
        return HttpUtil.createGet(host).header("Cookie", cookie).execute().body();
    }
    
    public static SongDetail getSongDetail(List<Integer> mudisId, String cookie) {
        String request = getRequest(host + "/song/detail?ids=" + ArrayUtil.join(mudisId.toArray(), ","), cookie);
        return JSON.parseObject(request, SongDetail.class);
    }
}
