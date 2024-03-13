package org.core.oss.service.impl.alist.model.remove.res;


import com.fasterxml.jackson.annotation.JsonProperty;

public class RemoveRes {
    
    @JsonProperty("code")
    private int code;
    
    @JsonProperty("data")
    private Object data;
    
    @JsonProperty("message")
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