package org.plugin.service;

import org.core.pojo.TbPluginMsgPojo;
import org.core.pojo.TbPluginTaskPojo;
import org.plugin.converter.PluginLabelValue;
import org.plugin.converter.PluginMsgRes;
import org.plugin.converter.PluginReq;
import org.plugin.converter.PluginRes;
import org.plugin.model.PluginRunParamsRes;
import org.plugin.model.PluginTaskLogRes;

import java.util.List;

public interface PluginService {
    List<PluginRes> getAllPlugin(Long userId, List<Long> pluginId, String name);
    
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
    PluginRunParamsRes getPluginParams(Long pluginId);
    
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
    
    TbPluginTaskPojo getTbPluginTaskPojo(Long pluginId, List<PluginLabelValue> pluginLabelValue, Long userId);
    
    List<TbPluginTaskPojo> getTask(Long id, String type, TbPluginTaskPojo taskPojo);
    
    /**
     * 删除插件任务
     * 注意： 目前只能删除已完成或错误停止的任务
     *
     * @param id 任务ID
     */
    void deleteTask(List<Long> id);
    
    /**
     * 删除插件
     *
     * @param id 插件ID
     */
    void deletePlugin(Long id);
    
    /**
     * 聚合插件搜索
     *
     * @param pluginLabelValue 程序启动插件
     * @param pluginId         插件ID
     * @param name             搜索参数
     * @return 搜索返回数据
     */
    List<PluginLabelValue> getInteractiveSearch(List<PluginLabelValue> pluginLabelValue, Long pluginId, String name);
    
    /**
     * 运行聚合插件
     *
     * @param pluginLabelValue 插件启动入参
     * @param pluginId         插件ID
     * @param type             传入ID类型 Music ID Album ID Artist ID
     * @param id               ID
     * @param pojo             任务ID
     * @return 运行完成同步返回信息
     */
    PluginTaskLogRes execInteractivePluginTask(List<PluginLabelValue> pluginLabelValue, Long pluginId, String type, Long id, TbPluginTaskPojo pojo);
}
