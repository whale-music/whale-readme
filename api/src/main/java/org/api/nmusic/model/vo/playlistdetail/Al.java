package org.api.nmusic.model.vo.playlistdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Al {
	
	@JsonProperty("picUrl")
	private String picUrl;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("tns")
	private List<Object> tns;
	
	@JsonProperty("pic_str")
	private String picStr;
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("pic")
	private Long pic;
}
