package org.api.nmusic.model.vo.playlistdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class L {
	
	@JsonProperty("br")
	private Integer br;
	
	@JsonProperty("fid")
	private Integer fid;
	
	@JsonProperty("size")
	private Integer size;
	
	@JsonProperty("vd")
	private Integer vd;
	
	@JsonProperty("sr")
	private Integer sr;
}
