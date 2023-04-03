package org.plugin.service;

import org.core.pojo.TbPluginMsgPojo;
import org.core.pojo.TbPluginTaskPojo;
import org.plugin.model.res.PluginLabelValue;
import org.plugin.model.res.PluginMsgRes;
import org.plugin.model.res.PluginReq;
import org.plugin.model.res.PluginRes;

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
     * @param pluginId 插件ID
     * @param onLine
     * @param id
     */
    void execPluginTask(Long pluginId, Boolean onLine, Long id);
    
    List<PluginMsgRes> getPluginRuntimeMessages(Long runtimeId);
    
    List<TbPluginMsgPojo> onLineExecPluginTask(Long pluginId, Long id);
    
    TbPluginTaskPojo getTbPluginTaskPojo(Long pluginId);
}
