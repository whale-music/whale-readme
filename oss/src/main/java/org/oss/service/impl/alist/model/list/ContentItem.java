package org.oss.service.impl.alist.model.list;

public class ContentItem {
    private Integer size;
    private String thumb;
    private Boolean isDir;
    private String name;
    private String sign;
    private String modified;
    private Integer type;
    
    public Integer getSize() {
        return size;
    }
    
    public void setSize(Integer size) {
        this.size = size;
    }
    
    public String getThumb() {
        return thumb;
    }
    
    public void setThumb(String thumb) {
        this.thumb = thumb;
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
    
    public Integer getType() {
        return type;
    }
    
    public void setType(Integer type) {
        this.type = type;
    }
}
