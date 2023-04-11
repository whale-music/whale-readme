package org.plugin.common;

public class TaskStatus {
    // 运行停止
    public static final short STOP_STATUS = (short) 0;
    // 运行中
    public static final short RUN_STATUS = (short) 1;
    // 运行错误
    public static final short ERROR_STATUS = (short) 2;
    
    private TaskStatus() {
    }
    
}
