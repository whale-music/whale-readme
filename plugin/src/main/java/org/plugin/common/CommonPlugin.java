package org.plugin.common;


import org.apache.commons.lang3.StringUtils;
import org.plugin.converter.PluginLabelValue;
import org.plugin.service.impl.PluginPackage;

import java.util.List;

public interface CommonPlugin extends PluginType {
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
        return org.core.config.PluginType.COMMON;
    }
    
    /**
     * 获取插件调用参数
     *
     * @return 参数
     */
    List<PluginLabelValue> getParams();
    
    /**
     * 执行方法
     *
     * @param values        方法自定参数
     * @param pluginPackage 插件调用服务
     */
    void apply(List<PluginLabelValue> values, PluginPackage pluginPackage);
    
    
    default String getValue(List<PluginLabelValue> values, String key) {
        return values.parallelStream()
                     .filter(pluginLabelValue -> StringUtils.equals(key, pluginLabelValue.getKey()))
                     .findFirst()
                     .orElseThrow(RuntimeException::new)
                     .getValue();
    }
    
}
