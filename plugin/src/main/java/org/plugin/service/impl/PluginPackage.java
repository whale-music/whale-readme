package org.plugin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.model.req.ArtistReq;
import org.api.admin.model.req.AudioInfoReq;
import org.api.admin.service.MusicFlowApi;
import org.core.pojo.MusicDetails;
import org.core.pojo.TbPluginMsgPojo;
import org.core.pojo.TbPluginTaskPojo;
import org.core.service.QukuService;
import org.core.service.TbPluginMsgService;
import org.core.service.TbPluginTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

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
    
    @Autowired
    private TbPluginMsgService pluginMsgService;
    
    @Autowired
    private TbPluginTaskService pluginTaskService;
    
    public String saveMusic(String json) throws IOException {
        AudioInfoReq dto = JSON.parseObject(json, AudioInfoReq.class);
        for (ArtistReq artist : dto.getArtists()) {
            JSONArray array = JSON.parseArray(artist.getAliasName());
            artist.setAliasName(CollUtil.join(array, ","));
        }
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
    
    public void log(String taskId, String userId, Object o) {
        log.info(o.toString());
        Long taskIdLong = Long.valueOf(taskId);
        TbPluginMsgPojo entity = new TbPluginMsgPojo();
        TbPluginTaskPojo taskServiceById = pluginTaskService.getById(taskIdLong);
        entity.setPluginId(taskServiceById.getPluginId());
        entity.setTaskId(taskIdLong);
        entity.setMsg(o.toString());
        entity.setUserId(Long.valueOf(Optional.ofNullable(userId).orElse("")));
        pluginMsgService.save(entity);
    }
    
    public static String getDate() {
        return cn.hutool.core.date.DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN);
    }
    
}
