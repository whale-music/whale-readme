package org.plugin.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.model.req.AudioInfoReq;
import org.api.admin.service.MusicFlowApi;
import org.core.pojo.MusicDetails;
import org.core.pojo.TbPluginMsgPojo;
import org.core.pojo.TbPluginTaskPojo;
import org.core.service.TbPluginMsgService;
import org.core.service.TbPluginTaskService;
import org.plugin.common.Func;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 插件包装类转换方法调用
 */
// @Service
@Slf4j
public class PluginPackage {
    
    private final MusicFlowApi musicFlowApi;
    
    private final TbPluginMsgService pluginMsgService;
    
    private final TbPluginTaskService pluginTaskService;
    private final List<TbPluginMsgPojo> msgPackages = new ArrayList<>();
    private Long taskId;
    private Long userId;
    private Func func;
    
    public PluginPackage(MusicFlowApi musicFlowApi, TbPluginMsgService pluginMsgService, TbPluginTaskService pluginTaskService, Long taskId, Long userId, Func func) {
        this.musicFlowApi = musicFlowApi;
        this.pluginMsgService = pluginMsgService;
        this.pluginTaskService = pluginTaskService;
        this.taskId = taskId;
        this.userId = userId;
        this.func = func;
    }
    
    public Func getFunc() {
        return func;
    }
    
    public void setFunc(Func func) {
        this.func = func;
    }
    
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public List<TbPluginMsgPojo> getLogs() {
        return msgPackages;
    }
    
    public MusicDetails saveMusic(AudioInfoReq dto) throws IOException {
        return musicFlowApi.saveMusicInfo(dto);
    }
    
    public void log(String format, Object... arguments) {
        log.info(format, arguments);
        TbPluginMsgPojo entity = new TbPluginMsgPojo();
        TbPluginTaskPojo taskServiceById = pluginTaskService.getById(taskId);
        entity.setPluginId(taskServiceById.getPluginId());
        entity.setTaskId(taskId);
        entity.setMsg(CharSequenceUtil.format(format, arguments));
        entity.setUserId(userId);
        
        msgPackages.add(entity);
        pluginMsgService.save(entity);
    }
}
