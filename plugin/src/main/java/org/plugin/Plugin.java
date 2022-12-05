package org.plugin;

public interface Plugin {
    
    /**
     * 方法执行前
     */
    void before();
    
    /**
     * 方法执行
     */
    String start(String json);
    
    /**
     * 方法执行后
     */
    void after();
}
