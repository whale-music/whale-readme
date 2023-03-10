package org.oss.service.impl.alist.util;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson2.JSON;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.jetbrains.annotations.NotNull;
import org.oss.service.impl.alist.model.address.Data;
import org.oss.service.impl.alist.model.address.MusicAddressReq;
import org.oss.service.impl.alist.model.address.MusicAddressRes;
import org.oss.service.impl.alist.model.list.ContentItem;
import org.oss.service.impl.alist.model.list.MusicListReq;
import org.oss.service.impl.alist.model.list.MusicListRes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Request {
    
    private static final Log log = Log.get();
    
    private Request() {
    }
    
    /**
     * 通用请求
     */
    @NotNull
    private static String req(String host, String body) {
        try (HttpResponse execute = HttpUtil.createPost(host).body(body).header("Cookie", null).execute()) {
            return execute.body();
        } catch (HttpException e) {
            throw new HttpException("http请求失败" + e);
        }
    }
    
    
    public static String getMusicAddress(String host, String objectSave, String path) {
        MusicAddressReq musicAddressReq = new MusicAddressReq();
        musicAddressReq.setPath('/' + objectSave + '/' + path);
        try {
            String resStr = req(host + "/api/fs/get", JSON.toJSONString(musicAddressReq));
            MusicAddressRes res = JSON.parseObject(resStr, MusicAddressRes.class);
            return Optional.ofNullable(res.getData()).orElse(new Data()).getSign();
        } catch (Exception e) {
            log.error("获取音乐错误{}\n{}", e.getMessage(), e.getStackTrace());
            throw new BaseException(ResultCode.SONG_NOT_EXIST);
        }
    }
    
    public static List<String> list(String host, String path) {
        MusicListReq musicListReq = new MusicListReq();
        musicListReq.setPath(path);
        musicListReq.setPage(1);
        musicListReq.setPerPage(0);
        try {
            String resStr = req(host + "/api/fs/list", JSON.toJSONString(musicListReq));
            MusicListRes res = JSON.parseObject(resStr, MusicListRes.class);
            return res.getData().getContent().stream().map(ContentItem::getName).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取音乐错误{}\n{}", e.getMessage(), e.getStackTrace());
            return ListUtil.empty();
        }
    }
}
