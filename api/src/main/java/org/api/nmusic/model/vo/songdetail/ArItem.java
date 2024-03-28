package org.api.nmusic.model.vo.songdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ArItem {
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("tns")
	private List<Object> tns;
	
	@JsonProperty("alias")
	private List<String> alias;
	
	@JsonProperty("id")
	private Long id;
}
