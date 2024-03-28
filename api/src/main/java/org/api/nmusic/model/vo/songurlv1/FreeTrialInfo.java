package org.api.nmusic.model.vo.songurlv1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FreeTrialInfo {
    
    @JsonProperty("algData")
    private Object algData;
    
    @JsonProperty("start")
    private Integer start;
    
    @JsonProperty("end")
    private Integer end;
    
    @JsonProperty("fragmentType")
    private Integer fragmentType;
}
