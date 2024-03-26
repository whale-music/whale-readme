package org.core.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.common.exception.BaseErrorInfoInterface;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NeteaseResult extends HashMap<String, Object> {
    /**
     * 响应代码
     */
    private Integer code;
    private String message;
    
    /**
     * 成功
     */
    public NeteaseResult success() {
        this.code = Integer.valueOf(ResultCode.SUCCESS.getCode());
        this.message = ResultCode.SUCCESS.getResultMsg();
        put("code", code);
        put("msg", message);
        return this;
    }
    
    /**
     * 成功
     */
    public NeteaseResult success(Object o) {
        this.code = Integer.valueOf(ResultCode.SUCCESS.getCode());
        this.message = ResultCode.SUCCESS.getResultMsg();
        put("data", o);
        put("code", code);
        put("msg", message);
        return this;
    }
    
    /**
     * 失败
     */
    public NeteaseResult error(String code) {
        this.code = Integer.valueOf(code);
        put("code", Integer.valueOf(code));
        return this;
    }
    
    /**
     * 失败,填入信息
     */
    public NeteaseResult error(String code, String message) {
        this.code = Integer.valueOf(code);
        this.message = message;
        put("code", Integer.valueOf(code));
        put("msg", message);
        return this;
    }
    
    /**
     * 失败
     */
    public NeteaseResult error(BaseErrorInfoInterface resultCode) {
        return error(resultCode.getCode(), resultCode.getResultMsg());
    }
}
