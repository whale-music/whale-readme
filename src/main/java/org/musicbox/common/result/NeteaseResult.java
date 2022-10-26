package org.musicbox.common.result;

import org.musicbox.common.exception.ResultCode;

import java.util.HashMap;

public class NeteaseResult extends HashMap<String, Object> {
    /**
     * 响应代码
     */
    private String code;
    private String message;
    
    /**
     * 成功
     */
    public NeteaseResult success() {
        this.code = ResultCode.SUCCESS.getResultCode();
        this.message = ResultCode.SUCCESS.getResultMsg();
        put("code", code);
        put("message", message);
        return this;
    }
    
    /**
     * 失败
     */
    public NeteaseResult error(String code) {
        this.code = code;
        put("code", code);
        return this;
    }
    
    /**
     * 失败,填入信息
     */
    public NeteaseResult error(String code, String message) {
        this.code = code;
        this.message = message;
        put("code", code);
        put("message ", message);
        return this;
    }
}
