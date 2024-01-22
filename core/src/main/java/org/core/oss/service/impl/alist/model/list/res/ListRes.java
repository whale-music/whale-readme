package org.core.oss.service.impl.alist.model.list.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ListRes {
    
    @JsonProperty("code")
    private Integer code;
    
    @JsonProperty("data")
    private Data data;
    
    @JsonProperty("message")
    private String message;
    
    public Integer getCode() {
        return code;
    }
    
    public Data getData() {
        return data;
    }
    
    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        return
                "ListReq{" +
                        "code = '" + code + '\'' +
                        ",data = '" + data + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}