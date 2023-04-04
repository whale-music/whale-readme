package org.plugin.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.model.req.AudioInfoReq;
import org.api.admin.service.MusicFlowApi;
import org.core.pojo.MusicDetails;
import org.core.service.QukuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * 插件包装类转换方法调用
 */
@Service
@Slf4j
public class PluginPackage {
    
    @Autowired
    private QukuService qukuService;
    
    @Autowired
    private MusicFlowApi musicFlowApi;
    
    public QukuService getQukuService() {
        return qukuService;
    }
    
    public MusicFlowApi getMusicFlowApi() {
        return musicFlowApi;
    }
    
    public String toJSONString(Object obj) {
        return JSON.toJSONString(obj);
    }
    
    public String saveMusic(String json) throws IOException {
        AudioInfoReq dto = JSON.parseObject(json, AudioInfoReq.class);
        MusicDetails musicDetails = musicFlowApi.saveMusicInfo(dto);
        return JSON.toJSONString(musicDetails);
    }
    
    public String get(String http, String cookie) {
        try (HttpResponse execute = HttpUtil.createRequest(Method.GET, http).header("Cookie", cookie).execute()) {
            return execute.body();
        } catch (HttpException e) {
            throw new HttpException("http请求失败" + e);
        }
    }
    
    public String post(String http, String cookie) {
        try (HttpResponse execute = HttpUtil.createRequest(Method.POST, http).header("Cookie", cookie).execute()) {
            return execute.body();
        } catch (HttpException e) {
            throw new HttpException("http请求失败" + e);
        }
    }
    
    public static void print(Object o) {
        log.info(o.toString());
    }
    
    public static String getDate() {
        return cn.hutool.core.date.DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN);
    }
    
}
