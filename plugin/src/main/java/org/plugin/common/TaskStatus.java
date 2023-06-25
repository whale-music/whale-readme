package org.plugin.common;

public class TaskStatus {
    // 运行停止
    public static final byte STOP_STATUS = 0;
    // 运行中
    public static final byte RUN_STATUS = 1;
    // 运行错误
    public static final byte ERROR_STATUS = 2;
    
    private TaskStatus() {
    }
    
}
