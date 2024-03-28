package org.api.nmusic.model.vo.playlistdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackIdsItem {
	
	@JsonProperty("sc")
	private Object sc;
	
	@JsonProperty("uid")
	private Integer uid;
	
	@JsonProperty("at")
	private Long at;
	
	@JsonProperty("t")
	private Integer t;
	
	@JsonProperty("v")
	private Integer v;
	
	@JsonProperty("f")
	private Object f;
	
	@JsonProperty("rcmdReason")
	private String rcmdReason;
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("alg")
	private Object alg;
	
	@JsonProperty("sr")
	private Object sr;
}
