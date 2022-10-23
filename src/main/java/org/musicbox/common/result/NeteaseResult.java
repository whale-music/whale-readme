package org.musicbox.common.result;

import org.musicbox.common.exception.ExceptionEnum;

import java.util.HashMap;

public class NeteaseResult extends HashMap<String, Object> {
    /**
     * 响应代码
     */
    private String code;
    
    /**
     * 成功
     */
    public NeteaseResult success() {
        this.code = ExceptionEnum.SUCCESS.getResultCode();
        return this;
    }
    
    /**
     * 失败
     */
    public NeteaseResult error(String code) {
        this.code = code;
        return this;
    }
}
