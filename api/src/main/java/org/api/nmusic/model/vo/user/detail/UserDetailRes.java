package org.api.nmusic.model.vo.user.detail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDetailRes {
	
	@JsonProperty("userPoint")
	private UserPoint userPoint;
	
	@JsonProperty("code")
	private Integer code;
	
	@JsonProperty("level")
	private Integer level;
	
	@JsonProperty("listenSongs")
	private Integer listenSongs;
	
	@JsonProperty("createDays")
	private Integer createDays;
	
	@JsonProperty("profile")
	private Profile profile;
	
	@JsonProperty("bindings")
	private List<BindingsItem> bindings;
	
	@JsonProperty("pcSign")
	private Boolean pcSign;
	
	@JsonProperty("mobileSign")
	private Boolean mobileSign;
	
	@JsonProperty("profileVillageInfo")
	private ProfileVillageInfo profileVillageInfo;
	
	@JsonProperty("adValid")
	private Boolean adValid;
	
	@JsonProperty("createTime")
	private Long createTime;
	
	@JsonProperty("recallUser")
	private Boolean recallUser;
	
	@JsonProperty("newUser")
	private Boolean newUser;
	
	@JsonProperty("peopleCanSeeMyPlayRecord")
	private Boolean peopleCanSeeMyPlayRecord;
	
	@Getter
	@Setter
	public static class UserPoint {
		
		@JsonProperty("balance")
		private Integer balance;
		
		@JsonProperty("blockBalance")
		private Integer blockBalance;
		
		@JsonProperty("updateTime")
		private Long updateTime;
		
		@JsonProperty("userId")
		private Long userId;
		
		@JsonProperty("version")
		private Integer version;
		
		@JsonProperty("status")
		private Integer status;
	}
	
	@Getter
	@Setter
	public static class Profile {
		
		@JsonProperty("backgroundUrl")
		private String backgroundUrl;
		
		@JsonProperty("birthday")
		private Long birthday;
		
		@JsonProperty("detailDescription")
		private String detailDescription;
		
		@JsonProperty("privacyItemUnlimit")
		private PrivacyItemUnlimit privacyItemUnlimit;
		
		@JsonProperty("gender")
		private Integer gender;
		
		@JsonProperty("city")
		private Integer city;
		
		@JsonProperty("signature")
		private String signature;
		
		@JsonProperty("followeds")
		private Integer followeds;
		
		@JsonProperty("description")
		private String description;
		
		@JsonProperty("remarkName")
		private Object remarkName;
		
		@JsonProperty("eventCount")
		private Integer eventCount;
		
		@JsonProperty("allSubscribedCount")
		private Integer allSubscribedCount;
		
		@JsonProperty("playlistBeSubscribedCount")
		private Integer playlistBeSubscribedCount;
		
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
		
		@JsonProperty("followMe")
		private Boolean followMe;
		
		@JsonProperty("artistIdentity")
		private List<Object> artistIdentity;
		
		@JsonProperty("nickname")
		private String nickname;
		
		@JsonProperty("expertTags")
		private Object expertTags;
		
		@JsonProperty("sDJPCount")
		private Integer sDJPCount;
		
		@JsonProperty("newFollows")
		private Integer newFollows;
		
		@JsonProperty("djStatus")
		private Integer djStatus;
		
		@JsonProperty("avatarUrl")
		private String avatarUrl;
		
		@JsonProperty("authStatus")
		private Integer authStatus;
		
		@JsonProperty("follows")
		private Integer follows;
		
		@JsonProperty("vipType")
		private Integer vipType;
		
		@JsonProperty("blacklist")
		private Boolean blacklist;
		
		@JsonProperty("followed")
		private Boolean followed;
		
		@JsonProperty("userId")
		private Long userId;
		
		@JsonProperty("mutual")
		private Boolean mutual;
		
		@JsonProperty("avatarImgId_str")
		private String avatarImgId_Str;
		
		@JsonProperty("followTime")
		private Object followTime;
		
		@JsonProperty("createTime")
		private Long createTime;
		
		@JsonProperty("authority")
		private Integer authority;
		
		@JsonProperty("cCount")
		private Integer cCount;
		
		@JsonProperty("inBlacklist")
		private Boolean inBlacklist;
		
		@JsonProperty("backgroundImgId")
		private Long backgroundImgId;
		
		@JsonProperty("userType")
		private Integer userType;
		
		@JsonProperty("avatarDetail")
		private Object avatarDetail;
		
		@JsonProperty("experts")
		private Experts experts;
		
		@JsonProperty("playlistCount")
		private Integer playlistCount;
		
		@JsonProperty("sCount")
		private Integer sCount;
		
		@Getter
		@Setter
		public static class PrivacyItemUnlimit {
			
			@JsonProperty("area")
			private Boolean area;
			
			@JsonProperty("college")
			private Boolean college;
			
			@JsonProperty("gender")
			private Boolean gender;
			
			@JsonProperty("age")
			private Boolean age;
			
			@JsonProperty("villageAge")
			private Boolean villageAge;
		}
		
		@Getter
		@Setter
		public static class Experts {
		
		}
		
		
	}
	
	@Getter
	@Setter
	public static class ProfileVillageInfo {
		
		@JsonProperty("imageUrl")
		private Object imageUrl;
		
		@JsonProperty("title")
		private String title;
		
		@JsonProperty("targetUrl")
		private String targetUrl;
	}
	
	@Getter
	@Setter
	public static class BindingsItem {
		
		@JsonProperty("expiresIn")
		private Integer expiresIn;
		
		@JsonProperty("expired")
		private Boolean expired;
		
		@JsonProperty("tokenJsonStr")
		private Object tokenJsonStr;
		
		@JsonProperty("refreshTime")
		private Integer refreshTime;
		
		@JsonProperty("id")
		private Integer id;
		
		@JsonProperty("type")
		private Integer type;
		
		@JsonProperty("bindingTime")
		private Long bindingTime;
		
		@JsonProperty("userId")
		private Integer userId;
		
		@JsonProperty("url")
		private String url;
	}
	
	
}
