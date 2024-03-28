package org.api.nmusic.model.vo.playlistdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlayListDetailRes {
	
	@JsonProperty("privileges")
	private List<PrivilegesItem> privileges;
	
	@JsonProperty("urls")
	private Object urls;
	
	@JsonProperty("resEntrance")
	private Object resEntrance;
	
	@JsonProperty("code")
	private Integer code;
	
	@JsonProperty("playlist")
	private Playlist playlist;
	
	@JsonProperty("fromUserCount")
	private Integer fromUserCount;
	
	@JsonProperty("relatedVideos")
	private Object relatedVideos;
	
	@JsonProperty("songFromUsers")
	private Object songFromUsers;
	
	@JsonProperty("sharedPrivilege")
	private Object sharedPrivilege;
	
	@JsonProperty("fromUsers")
	private Object fromUsers;
}
