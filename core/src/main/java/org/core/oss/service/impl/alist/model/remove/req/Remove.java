package org.core.oss.service.impl.alist.model.remove.req;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class Remove {
    
    @JsonProperty("names")
    private List<String> names;
    
    @JsonProperty("dir")
    private String dir;
}