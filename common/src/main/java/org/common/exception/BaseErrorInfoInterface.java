package org.common.exception;

/**
 * 服务接口类
 */
public interface BaseErrorInfoInterface {
    
    /**
     * 错误码
     */
    String getCode();
    
    /**
     * 错误描述
     */
    String getResultMsg();
}
