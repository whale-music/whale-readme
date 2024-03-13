package org.core.oss.service.impl.alist.util;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.http.*;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.oss.model.Resource;
import org.core.oss.service.impl.alist.model.login.req.LoginReq;
import org.core.oss.service.impl.alist.model.login.res.DataRes;
import org.core.oss.service.impl.alist.model.login.res.LoginRes;
import org.core.oss.service.impl.alist.model.remove.req.Remove;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class RequestUtils {
    
    private RequestUtils() {
    }
    
    /**
     * 通用请求
     */
    @NotNull
    private static String req(String host, String body, Map<String, String> headers) {
        try (HttpResponse execute = HttpUtil.createPost(host).body(body).headerMap(headers, true).execute()) {
            return execute.body();
        } catch (IORuntimeException e) {
            log.error(e.getMessage(), e);
            throw new BaseException(ResultCode.OSS_CONNECT_ERROR);
        } catch (HttpException e) {
            throw new HttpException("http请求失败" + e);
        }
    }
    
    /**
     * 通用请求
     */
    @NotNull
    private static String req(String host, File formFile, Map<String, String> headers) {
        int milliseconds = 60 * 60 * 1000;
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
    
    public static String login(String host, String accessKey, String secretKey) {
        LoginReq req = new LoginReq();
        req.setUsername(accessKey);
        req.setPassword(secretKey);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String resStr = req(host + "/api/auth/login", objectMapper.writeValueAsString(req), null);
            LoginRes loginRes = objectMapper.readValue(resStr, LoginRes.class);
            return Optional.ofNullable(loginRes.getData()).orElse(new DataRes()).getToken();
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    
    public static String upload(String host, String objectSaveConfig, File srcFile, String loginJwtCache) {
        String url = objectSaveConfig + "/" + srcFile.getName();
        String musicAddress = URLEncodeUtil.encodeAll(url);
        HashMap<String, String> headers = getHeaders(host, objectSaveConfig, loginJwtCache);
        headers.put("File-Path", musicAddress);
        
        String resJson = req(host + "/api/fs/form", srcFile, headers);
        
        JSONObject parseObject = JSONUtil.parseObj(resJson);
        if (!StringUtils.equals(String.valueOf(parseObject.get("code")), String.valueOf(200))) {
            throw new BaseException(ResultCode.OSS_UPLOAD_ERROR);
        }
        log.debug("上传成功: {},地址: {}", srcFile.getName(), url);
        return url;
    }
    
    @NotNull
    private static HashMap<String, String> getHeaders(String host, String path, String loginJwtCache) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json, text/plain, */*");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        headers.put("Authorization", loginJwtCache);
        headers.put("Connection", "keep-alive");
        headers.put("Origin", host);
        headers.put("Password", "");
        headers.put("Referer", host + path);
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");
        return headers;
    }
    
    public static void delete(String host, Map<String, List<Resource>> item, String loginCacheStr) {
        for (Map.Entry<String, List<Resource>> entry : item.entrySet()) {
            HashMap<String, String> headers = getHeaders(host, entry.getKey(), loginCacheStr);
            
            Remove remove = new Remove();
            remove.setDir(entry.getKey());
            remove.setNames(entry.getValue().parallelStream().map(Resource::getName).toList());
            
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String req = req(host + "/api/fs/remove", objectMapper.writeValueAsString(remove), headers);
                Object code = objectMapper.readValue(req, Map.class).get("code");
                if (code == null || Integer.parseInt(String.valueOf(code)) != 200) {
                    throw new BaseException(ResultCode.OSS_REMOVE_ERROR);
                }
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }
    
    public static void rename(String host, String loginCacheStr, String path, String newName) {
        HashMap<String, String> headers = getHeaders(host, "", loginCacheStr);
        HashMap<String, String> reqBodyMap = new HashMap<>();
        reqBodyMap.put("path", path);
        reqBodyMap.put("name", newName);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String req = req(host + "/api/fs/rename", objectMapper.writeValueAsString(reqBodyMap), headers);
            Object code = objectMapper.readValue(req, Map.class).get("code");
            if (code == null || Integer.parseInt(String.valueOf(code)) != 200) {
                throw new BaseException(ResultCode.OSS_REMOVE_ERROR);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
