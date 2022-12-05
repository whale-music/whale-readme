package org.plugin;

public interface Plugin {
    
    /**
     * 方法执行前
     */
    void before();
    
    /**
     * 方法执行
     */
    void start();
    
    /**
     * 方法执行后
     */
    void after();
}
