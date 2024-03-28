package org.api.nmusic.model.vo.songdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
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
