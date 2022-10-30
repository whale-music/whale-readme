package org.musicbox.exception;

/**
 * @description: 自定义异常类
 * @author: DT
 * @date: 2021/4/19 21:44
 * @version: v1.0
 */
public class NoAuthorityException extends BaseException {
    
    public NoAuthorityException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }
    
}
