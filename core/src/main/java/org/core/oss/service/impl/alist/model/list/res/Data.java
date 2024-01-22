package org.core.oss.service.impl.alist.model.list.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Data {
    
    @JsonProperty("total")
    private Integer total;
    
    @JsonProperty("provider")
    private String provider;
    
    @JsonProperty("readme")
    private String readme;
    
    @JsonProperty("write")
    private Boolean write;
    
    @JsonProperty("content")
    private List<ContentItem> content;
    
    public Integer getTotal() {
        return total;
    }
    
    public String getProvider() {
        return provider;
    }
    
    public String getReadme() {
        return readme;
    }
    
    public Boolean isWrite() {
        return write;
    }
    
    public List<ContentItem> getContent() {
        return content;
    }
    
    @Override
    public String toString() {
        return
                "Data{" +
                        "total = '" + total + '\'' +
                        ",provider = '" + provider + '\'' +
                        ",readme = '" + readme + '\'' +
                        ",write = '" + write + '\'' +
                        ",content = '" + content + '\'' +
                        "}";
    }
}