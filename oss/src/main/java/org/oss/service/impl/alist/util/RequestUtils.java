package org.oss.service.impl.alist.util;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.http.*;
import cn.hutool.log.Log;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.cglib.beans.BeanMap;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RequestUtils {
    
    private static final Log log = Log.get();
    
    private RequestUtils() {
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
    
    /**
     * 通用请求
     */
    @NotNull
    private static String req(String host, File formFile, Map<String, String> headers) {
        int milliseconds = 1000 * 600;
        HttpRequest request = HttpUtil.createRequest(Method.PUT, host);
        request.form("file", formFile);
        request.addHeaders(headers);
        request.setConnectionTimeout(milliseconds);
        request.setReadTimeout(milliseconds);
        request.timeout(milliseconds);
        try (HttpResponse execute = request.execute()) {
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
        musicListReq.setRefresh(true);
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
    
    public static String upload(String host, String objectSaveConfig, File srcFile, String loginJwtCache) {
        String url = objectSaveConfig + "/" + srcFile.getName();
        String musicAddress = URLEncodeUtil.encodeAll(url);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json, text/plain, */*");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        headers.put("Authorization", loginJwtCache);
        headers.put("Connection", "keep-alive");
        headers.put("File-Path", musicAddress);
        headers.put("Origin", host);
        headers.put("Password", "");
        headers.put("Referer", host + objectSaveConfig);
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");
        
        String resJson = req(host + "/api/fs/form", srcFile, headers);
        BeanMap map = BeanMap.create(resJson);
        if (!StringUtils.equals(String.valueOf(map.get("code")), String.valueOf(200))) {
            throw new BaseException(ResultCode.OSS_UPLOAD_ERROR);
        }
        log.debug("上传成功: {},地址: {}", srcFile.getName(), url);
        return FileUtil.mainName(srcFile.getName());
    }
}
