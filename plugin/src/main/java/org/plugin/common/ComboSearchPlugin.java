package org.plugin.common;

import org.apache.commons.lang3.StringUtils;
import org.plugin.converter.PluginLabelValue;
import org.plugin.service.impl.PluginPackage;

import java.util.List;

/**
 * 交互插件在音乐界面中使用
 */
public interface ComboSearchPlugin extends PluginType {
    /**
     * 获取插件类型
     * 普通插件
     * 交互插件
     * 聚合插件
     *
     * @return 插件类型
     */
    @Override
    default String getType() {
        return org.core.config.PluginType.INTERACTIVE;
    }
    
    /**
     * 获取插件调用参数
     *
     * @return 参数
     */
    List<PluginLabelValue> getParams();
    
    /**
     * 搜索
     * 返回值label已html解析
     *
     * @param params 运行参数
     * @param name   关键词
     * @return 搜索到的数据
     */
    List<PluginLabelValue> search(List<PluginLabelValue> params, String name);
    
    /**
     * 需要同步的数据
     * 返回结果以html解析
     *
     * @param data          数据
     * @param type          ID类型 可能没有。需要自行判断,类型可能是Music ID Album ID Artist ID
     * @param id            需要同步的ID。可能没有,需要自行判断。
     * @param pluginPackage 插件服务
     * @return 返回成功或失败数据。
     */
    String sync(List<PluginLabelValue> data, String type, Long id, PluginPackage pluginPackage);
    
    
    default String getValue(List<PluginLabelValue> values, String key) {
        return values.parallelStream()
                     .filter(pluginLabelValue -> StringUtils.equals(key, pluginLabelValue.getKey()))
                     .findFirst()
                     .orElseThrow(RuntimeException::new)
                     .getValue();
    }
}
