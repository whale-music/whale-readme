package org.plugin.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.model.req.AudioInfoReq;
import org.api.admin.service.MusicFlowApi;
import org.core.iservice.TbPluginMsgService;
import org.core.iservice.TbPluginTaskService;
import org.core.pojo.MusicDetails;
import org.core.pojo.TbPluginMsgPojo;
import org.core.pojo.TbPluginTaskPojo;
import org.core.service.QukuService;
import org.plugin.common.CommonPlugin;

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
    
    private final QukuService qukuService;
    
    private final List<TbPluginMsgPojo> msgPackages = new ArrayList<>();
    private final Long taskId;
    private final Long userId;
    private final CommonPlugin func;
    
    public PluginPackage(MusicFlowApi musicFlowApi, TbPluginMsgService pluginMsgService, TbPluginTaskService pluginTaskService, QukuService qukuService, Long taskId, Long userId, CommonPlugin func) {
        this.musicFlowApi = musicFlowApi;
        this.pluginMsgService = pluginMsgService;
        this.pluginTaskService = pluginTaskService;
        this.qukuService = qukuService;
        this.taskId = taskId;
        this.userId = userId;
        this.func = func;
    }
    
    public QukuService getQukuService() {
        return qukuService;
    }
    
    public List<TbPluginMsgPojo> getLogs() {
        return msgPackages;
    }
    
    public MusicDetails saveMusic(AudioInfoReq dto) throws IOException {
        return musicFlowApi.saveMusicInfo(dto);
    }
    
    public void log(short level, String format, Object... arguments) {
        log.info(format, arguments);
        TbPluginMsgPojo entity = new TbPluginMsgPojo();
        TbPluginTaskPojo taskServiceById = pluginTaskService.getById(taskId);
        entity.setPluginId(taskServiceById.getPluginId());
        entity.setTaskId(taskId);
        entity.setMsg(CharSequenceUtil.format(format, arguments));
        entity.setUserId(userId);
        entity.setLevel(level);
        
        msgPackages.add(entity);
        pluginMsgService.save(entity);
    }
    
    public void logInfo(String format, Object... arguments) {
        log((short) 0, format, arguments);
    }
    
    public void logDebug(String format, Object... arguments) {
        log((short) 1, format, arguments);
    }
    
    public void logWarn(String format, Object... arguments) {
        log((short) 2, format, arguments);
    }
    
    public void logError(String format, Object... arguments) {
        log((short) 3, format, arguments);
    }
}
