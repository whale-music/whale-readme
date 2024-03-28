package org.api.nmusic.model.vo.playlistdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Creator {
	
	@JsonProperty("birthday")
	private Integer birthday;
	
	@JsonProperty("detailDescription")
	private String detailDescription;
	
	@JsonProperty("backgroundUrl")
	private String backgroundUrl;
	
	@JsonProperty("gender")
	private Integer gender;
	
	@JsonProperty("city")
	private Integer city;
	
	@JsonProperty("signature")
	private String signature;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("remarkName")
	private Object remarkName;
	
	@JsonProperty("accountStatus")
	private Integer accountStatus;
	
	@JsonProperty("avatarImgId")
	private Long avatarImgId;
	
	@JsonProperty("defaultAvatar")
	private Boolean defaultAvatar;
	
	@JsonProperty("avatarImgIdStr")
	private String avatarImgIdStr;
	
	@JsonProperty("backgroundImgIdStr")
	private String backgroundImgIdStr;
	
	@JsonProperty("province")
	private Integer province;
	
	@JsonProperty("nickname")
	private String nickname;
	
	@JsonProperty("expertTags")
	private Object expertTags;
	
	@JsonProperty("djStatus")
	private Integer djStatus;
	
	@JsonProperty("avatarUrl")
	private String avatarUrl;
	
	@JsonProperty("authStatus")
	private Integer authStatus;
	
	@JsonProperty("vipType")
	private Integer vipType;
	
	@JsonProperty("followed")
	private Boolean followed;
	
	@JsonProperty("userId")
	private Long userId;
	
	@JsonProperty("authenticationTypes")
	private Integer authenticationTypes;
	
	@JsonProperty("mutual")
	private Boolean mutual;
	
	@JsonProperty("authority")
	private Integer authority;
	
	@JsonProperty("anchor")
	private Boolean anchor;
	
	@JsonProperty("userType")
	private Integer userType;
	
	@JsonProperty("backgroundImgId")
	private Long backgroundImgId;
	
	@JsonProperty("experts")
	private Object experts;
	
	@JsonProperty("avatarDetail")
	private Object avatarDetail;
}
