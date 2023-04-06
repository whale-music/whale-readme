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
import org.oss.service.impl.alist.model.login.req.LoginReq;
import org.oss.service.impl.alist.model.login.res.DataRes;
import org.oss.service.impl.alist.model.login.res.LoginRes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Request {
    
    private static final Log log = Log.get();
    
    private Request() {
    }
    
    /**
     * 通用请求
     */
    @NotNull
    private static String req(String host, String body, Map<String, List<String>> headers) {
        try (HttpResponse execute = HttpUtil.createPost(host).body(body).header(headers).execute()) {
            return execute.body();
        } catch (HttpException e) {
            throw new HttpException("http请求失败" + e);
        }
    }
    
    
    public static String getMusicAddress(String host, String objectSave, String path) {
        MusicAddressReq musicAddressReq = new MusicAddressReq();
        musicAddressReq.setPath('/' + objectSave + '/' + path);
        try {
            String resStr = req(host + "/api/fs/get", JSON.toJSONString(musicAddressReq), null);
            MusicAddressRes res = JSON.parseObject(resStr, MusicAddressRes.class);
            return Optional.ofNullable(res.getData()).orElse(new Data()).getSign();
        } catch (Exception e) {
            log.error("获取音乐错误{}\n{}", e.getMessage(), e.getStackTrace());
            throw new BaseException(ResultCode.SONG_NOT_EXIST);
        }
    }
    
    public static List<ContentItem> list(String host, String objectSave, Map<String, List<String>> headers) {
        MusicListReq musicListReq = new MusicListReq();
        musicListReq.setPath('/' + objectSave);
        musicListReq.setPage(1);
        musicListReq.setPerPage(0);
        try {
            String resStr = req(host + "/api/fs/list", JSON.toJSONString(musicListReq), headers);
            MusicListRes res = JSON.parseObject(resStr, MusicListRes.class);
            return res.getData().getContent();
        } catch (Exception e) {
            log.error("获取音乐错误{}\n{}", e.getMessage(), e.getStackTrace());
            return ListUtil.empty();
        }
    }
    
    public static String login(String host, String accessKey, String secretKey) {
        LoginReq req = new LoginReq();
        req.setUsername(accessKey);
        req.setPassword(secretKey);
        String resStr = req(host + "/api/auth/login", JSON.toJSONString(req), null);
        LoginRes loginRes = JSON.parseObject(resStr, LoginRes.class);
        return Optional.ofNullable(loginRes.getData()).orElse(new DataRes()).getToken();
    }
}
