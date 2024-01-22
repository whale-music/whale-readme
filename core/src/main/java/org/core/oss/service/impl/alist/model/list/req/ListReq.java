package org.core.oss.service.impl.alist.model.list.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ListReq {
    
    @JsonProperty("path")
    private String path;
    
    @JsonProperty("per_page")
    private Integer perPage;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("refresh")
    private Boolean refresh;
    
    @JsonProperty("page")
    private Integer page;
    
    @Override
    public String toString() {
        return
                "ListReq{" +
                        "path = '" + path + '\'' +
                        ",per_page = '" + perPage + '\'' +
                        ",password = '" + password + '\'' +
                        ",refresh = '" + refresh + '\'' +
                        ",page = '" + page + '\'' +
                        "}";
    }
}