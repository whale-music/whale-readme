package org.core.oss.service.impl.alist.model.list;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FsList {
    
    @JsonProperty("size")
    private Long size;
    
    @JsonProperty("thumb")
    private String thumb;
    
    @JsonProperty("is_dir")
    private Boolean isDir;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("path")
    private String path;
    
    @JsonProperty("sign")
    private String sign;
    
    @JsonProperty("modified")
    private String modified;
    
    @JsonProperty("type")
    private Integer type;
    
    @Override
    public String toString() {
        return
                "FsList{" +
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