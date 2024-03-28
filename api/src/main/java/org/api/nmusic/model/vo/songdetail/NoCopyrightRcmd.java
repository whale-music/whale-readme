package org.api.nmusic.model.vo.songdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoCopyrightRcmd {
    
    @JsonProperty("typeDesc")
    private String typeDesc;
    
    @JsonProperty("expInfo")
    private Object expInfo;
    
    @JsonProperty("thirdPartySong")
    private Object thirdPartySong;
    
    @JsonProperty("type")
    private Integer type;
    
    @JsonProperty("songId")
    private Object songId;
}
