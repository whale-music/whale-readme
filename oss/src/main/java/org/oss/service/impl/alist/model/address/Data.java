package org.oss.service.impl.alist.model.address;

public class Data {
    private Integer size;
    private Object related;
    private String thumb;
    private String provider;
    private Boolean isDir;
    private String name;
    private String sign;
    private String modified;
    private String readme;
    private Integer type;
    private String raw_url;
    
    public Integer getSize() {
        return size;
    }
    
    public void setSize(Integer size) {
        this.size = size;
    }
    
    public Object getRelated() {
        return related;
    }
    
    public void setRelated(Object related) {
        this.related = related;
    }
    
    public String getThumb() {
        return thumb;
    }
    
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
    
    public String getProvider() {
        return provider;
    }
    
    public void setProvider(String provider) {
        this.provider = provider;
    }
    
    public void setIsDir(Boolean isDir) {
        this.isDir = isDir;
    }
    
    public Boolean isIsDir() {
        return isDir;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSign() {
        return sign;
    }
    
    public void setSign(String sign) {
        this.sign = sign;
    }
    
    public String getModified() {
        return modified;
    }
    
    public void setModified(String modified) {
        this.modified = modified;
    }
    
    public String getReadme() {
        return readme;
    }
    
    public void setReadme(String readme) {
        this.readme = readme;
    }
    
    public Integer getType() {
        return type;
    }
    
    public void setType(Integer type) {
        this.type = type;
    }
    
    public String getRawUrl() {
        return raw_url;
    }
    
    public void setRaw_url(String raw_url) {
        this.raw_url = raw_url;
    }
}
