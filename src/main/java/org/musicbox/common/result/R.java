package org.musicbox.common.result;

import com.alibaba.fastjson2.JSONObject;
import org.musicbox.common.exception.BaseErrorInfoInterface;

public class R {
    /**
     * 响应代码
     */
    private String code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应结果
     */
    private Object result;
    
    public R() {
    }
    
    public R(BaseErrorInfoInterface errorInfo) {
        this.code = errorInfo.getResultCode();
        this.message = errorInfo.getResultMsg();
    }
    
    /**
     * 成功
     *
     * @return
     */
    public static R success() {
        return success(null);
    }
    
    /**
     * 成功
     *
     * @param data
     * @return
     */
    public static R success(Object data) {
        R rb = new R();
        rb.setCode(ResultCode.SUCCESS.getResultCode());
        rb.setMessage(ResultCode.SUCCESS.getResultMsg());
        rb.setResult(data);
        return rb;
    }
    
    /**
     * 失败
     */
    public static R error(BaseErrorInfoInterface errorInfo) {
        R rb = new R();
        rb.setCode(errorInfo.getResultCode());
        rb.setMessage(errorInfo.getResultMsg());
        rb.setResult(null);
        return rb;
    }
    
    /**
     * 失败
     */
    public static R error(String code, String message) {
        R rb = new R();
        rb.setCode(code);
        rb.setMessage(message);
        rb.setResult(null);
        return rb;
    }
    
    /**
     * 失败
     */
    public static R error(String message) {
        R rb = new R();
        rb.setCode("-1");
        rb.setMessage(message);
        rb.setResult(null);
        return rb;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Object getResult() {
        return result;
    }
    
    public void setResult(Object result) {
        this.result = result;
    }
    
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
    
}
