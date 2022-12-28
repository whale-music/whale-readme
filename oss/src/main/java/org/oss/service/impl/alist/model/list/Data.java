package org.oss.service.impl.alist.model.list;

import java.util.List;

public class Data {
    private Integer total;
    private String provider;
    private String readme;
    private Boolean write;
    private List<ContentItem> content;
    
    public Integer getTotal() {
        return total;
    }
    
    public void setTotal(Integer total) {
        this.total = total;
    }
    
    public String getProvider() {
        return provider;
    }
    
    public void setProvider(String provider) {
        this.provider = provider;
    }
    
    public String getReadme() {
        return readme;
    }
    
    public void setReadme(String readme) {
        this.readme = readme;
    }
    
    public void setWrite(Boolean write) {
        this.write = write;
    }
    
    public Boolean isWrite() {
        return write;
    }
    
    public List<ContentItem> getContent() {
        return content;
    }
    
    public void setContent(List<ContentItem> content) {
        this.content = content;
    }
}