package org.plugin.service;

import org.plugin.model.res.PluginLabelValue;
import org.plugin.model.res.PluginReq;
import org.plugin.model.res.PluginRes;

import java.util.List;

public interface PluginService {
    List<PluginRes> getAllPlugin(Long userId);
    
    /**
     * 添加插件代码
     *
     * @param req 保存或更新数据
     */
    void saveOrUpdatePlugin(PluginReq req);
    
    /**
     * 查询插件入参
     *
     * @param pluginId 插件ID
     * @return 插件入参
     */
    List<PluginLabelValue> getPluginParams(String pluginId);
    
    /**
     * 运行插件任务
     *
     * @param pluginId 插件ID
     * @param req      插件入参
     */
    void execPluginTask(String pluginId, List<PluginLabelValue> req);
}
