package org.api.nmusic.model.vo.playlistdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
