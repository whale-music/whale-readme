package org.api.nmusic.model.vo.songurlv1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FreeTrialPrivilege {
    public FreeTrialPrivilege() {
    }
    
    public FreeTrialPrivilege(Boolean userConsumable, Boolean resConsumable, Integer cannotListenReason, Object playReason, Integer listenType) {
        this.userConsumable = userConsumable;
        this.resConsumable = resConsumable;
        this.cannotListenReason = cannotListenReason;
        this.playReason = playReason;
        this.listenType = listenType;
    }
    
    @JsonProperty("userConsumable")
    private Boolean userConsumable;
    
    @JsonProperty("resConsumable")
    private Boolean resConsumable;
    
    @JsonProperty("cannotListenReason")
    private Integer cannotListenReason;
    
    @JsonProperty("playReason")
    private Object playReason;
    
    @JsonProperty("listenType")
    private Integer listenType;
}
