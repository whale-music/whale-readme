package org.api.nmusic.model.vo.songdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SongDetailRes {
	
	@JsonProperty("privileges")
	private List<PrivilegesItem> privileges;
	
	@JsonProperty("code")
	private Integer code;
	
	@JsonProperty("songs")
	private List<SongsItem> songs;
}
