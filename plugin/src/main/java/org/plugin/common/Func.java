package org.plugin.common;


import org.plugin.converter.PluginLabelValue;
import org.plugin.service.impl.PluginPackage;

import java.util.List;

public interface Func {
    
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
    
}
