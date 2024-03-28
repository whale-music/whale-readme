package org.api.nmusic.model.vo.playlistdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChargeInfoListItem {
	
	@JsonProperty("rate")
	private Integer rate;
	
	@JsonProperty("chargeMessage")
	private Object chargeMessage;
	
	@JsonProperty("chargeType")
	private Integer chargeType;
	
	@JsonProperty("chargeUrl")
	private Object chargeUrl;
}
