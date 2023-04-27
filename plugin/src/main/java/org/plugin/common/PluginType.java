package org.plugin.common;

public interface PluginType {
    /**
     * 获取插件类型
     * 普通插件
     * 交互插件
     * 聚合插件
     *
     * @return 插件类型
     */
    String getType();
}
