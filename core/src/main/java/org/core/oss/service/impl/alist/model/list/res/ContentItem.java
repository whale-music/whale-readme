package org.core.oss.service.impl.alist.model.list.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContentItem {
    
    @JsonProperty("size")
    private Long size;
    
    @JsonProperty("thumb")
    private String thumb;
    
    @JsonProperty("is_dir")
    private Boolean isDir;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("sign")
    private String sign;
    
    @JsonProperty("modified")
    private String modified;
    
    @JsonProperty("type")
    private Integer type;
    
    public Long getSize() {
        return size;
    }
    
    public String getThumb() {
        return thumb;
    }
    
    
    public String getName() {
        return name;
    }
    
    public Boolean getDir() {
        return isDir;
    }
    
    public String getSign() {
        return sign;
    }
    
    public String getModified() {
        return modified;
    }
    
    public Integer getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return
                "ContentItem{" +
                        "size = '" + size + '\'' +
                        ",thumb = '" + thumb + '\'' +
                        ",is_dir = '" + isDir + '\'' +
                        ",name = '" + name + '\'' +
                        ",sign = '" + sign + '\'' +
                        ",modified = '" + modified + '\'' +
                        ",type = '" + type + '\'' +
                        "}";
    }
}