package org.core.oss.service.impl.alist.model.remove.res;

import com.alibaba.fastjson.annotation.JSONField;

public class RemoveRes {
    
    @JSONField(name = "code")
    private int code;
    
    @JSONField(name = "data")
    private Object data;
    
    @JSONField(name = "message")
    private String message;
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return
                "RemoveRes{" +
                        "code = '" + code + '\'' +
                        ",data = '" + data + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}