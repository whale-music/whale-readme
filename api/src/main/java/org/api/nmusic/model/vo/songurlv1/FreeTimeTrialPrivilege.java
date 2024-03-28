package org.api.nmusic.model.vo.songurlv1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FreeTimeTrialPrivilege {
    
    @JsonProperty("userConsumable")
    private Boolean userConsumable;
    
    @JsonProperty("resConsumable")
    private Boolean resConsumable;
    
    @JsonProperty("remainTime")
    private Integer remainTime;
    
    @JsonProperty("type")
    private Integer type;
}
