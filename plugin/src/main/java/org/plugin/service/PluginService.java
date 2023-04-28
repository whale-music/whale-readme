package org.plugin.service;

import org.core.pojo.TbPluginMsgPojo;
import org.core.pojo.TbPluginTaskPojo;
import org.plugin.converter.PluginLabelValue;
import org.plugin.converter.PluginMsgRes;
import org.plugin.converter.PluginReq;
import org.plugin.converter.PluginRes;

import java.util.List;

public interface PluginService {
    List<PluginRes> getAllPlugin(Long userId, Long pluginId);
    
    /**
     * 添加插件代码
     *
     * @param req 保存或更新数据
     */
    PluginRes saveOrUpdatePlugin(PluginReq req);
    
    /**
     * 查询插件入参
     *
     * @param pluginId 插件ID
     * @return 插件入参
     */
    List<PluginLabelValue> getPluginParams(Long pluginId);
    
    /**
     * 运行插件任务
     *
     * @param pluginLabelValue 插件入参
     * @param pluginId         插件ID
     * @param onLine           是否在线运行
     * @param taskId           任务ID
     */
    void execPluginTask(List<PluginLabelValue> pluginLabelValue, Long pluginId, Boolean onLine, TbPluginTaskPojo taskId);
    
    List<PluginMsgRes> getPluginRuntimeMessages(Long runtimeId);
    
    List<TbPluginMsgPojo> onLineExecPluginTask(List<PluginLabelValue> pluginLabelValue, Long pluginId, TbPluginTaskPojo id);
    
    TbPluginTaskPojo getTbPluginTaskPojo(Long pluginId, Long userId);
    
    List<TbPluginTaskPojo> getPluginRuntimeTask(TbPluginTaskPojo taskPojo);
}
