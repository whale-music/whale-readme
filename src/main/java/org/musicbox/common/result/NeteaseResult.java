package org.musicbox.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
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
        this.code = ResultCode.SUCCESS.getCode();
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
