package org.api.nmusic.model.vo.songurlv1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SongUrlV1 {
    
    @JsonProperty("code")
    private Integer code;
    
    @JsonProperty("data")
    private List<DataItem> data;
}
