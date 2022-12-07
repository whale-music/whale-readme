package org.plugin.service;

public interface Plugin {
    /**
     * 检查插件是否有效
     */
    void check();
    
    /**
     * 方法执行
     */
    String start(String json);
}
