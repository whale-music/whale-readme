package org.api.nmusic.model.vo.playlistdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArtistsItem {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("id")
    private Integer id;
}
