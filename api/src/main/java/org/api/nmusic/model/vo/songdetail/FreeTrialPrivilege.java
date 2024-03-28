package org.api.nmusic.model.vo.songdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FreeTrialPrivilege {
	
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
