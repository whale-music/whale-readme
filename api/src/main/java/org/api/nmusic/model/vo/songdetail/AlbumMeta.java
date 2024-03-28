package org.api.nmusic.model.vo.songdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AlbumMeta {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("id")
    private Integer id;
}
