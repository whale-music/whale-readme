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
	private int code;
	
	@JsonProperty("level")
	private int level;
	
	@JsonProperty("listenSongs")
	private int listenSongs;
	
	@JsonProperty("createDays")
	private int createDays;
	
	@JsonProperty("profile")
	private Profile profile;
	
	@JsonProperty("bindings")
	private List<BindingsItem> bindings;
	
	@JsonProperty("pcSign")
	private boolean pcSign;
	
	@JsonProperty("mobileSign")
	private boolean mobileSign;
	
	@JsonProperty("profileVillageInfo")
	private ProfileVillageInfo profileVillageInfo;
	
	@JsonProperty("adValid")
	private boolean adValid;
	
	@JsonProperty("createTime")
	private long createTime;
	
	@JsonProperty("recallUser")
	private boolean recallUser;
	
	@JsonProperty("newUser")
	private boolean newUser;
	
	@JsonProperty("peopleCanSeeMyPlayRecord")
	private boolean peopleCanSeeMyPlayRecord;
	
	@Getter
	@Setter
	public static class UserPoint {
		
		@JsonProperty("balance")
		private int balance;
		
		@JsonProperty("blockBalance")
		private int blockBalance;
		
		@JsonProperty("updateTime")
		private long updateTime;
		
		@JsonProperty("userId")
		private long userId;
		
		@JsonProperty("version")
		private int version;
		
		@JsonProperty("status")
		private int status;
	}
	
	@Getter
	@Setter
	public static class Profile {
		
		@JsonProperty("backgroundUrl")
		private String backgroundUrl;
		
		@JsonProperty("birthday")
		private long birthday;
		
		@JsonProperty("detailDescription")
		private String detailDescription;
		
		@JsonProperty("privacyItemUnlimit")
		private PrivacyItemUnlimit privacyItemUnlimit;
		
		@JsonProperty("gender")
		private int gender;
		
		@JsonProperty("city")
		private int city;
		
		@JsonProperty("signature")
		private String signature;
		
		@JsonProperty("followeds")
		private int followeds;
		
		@JsonProperty("description")
		private String description;
		
		@JsonProperty("remarkName")
		private Object remarkName;
		
		@JsonProperty("eventCount")
		private int eventCount;
		
		@JsonProperty("allSubscribedCount")
		private int allSubscribedCount;
		
		@JsonProperty("playlistBeSubscribedCount")
		private int playlistBeSubscribedCount;
		
		@JsonProperty("accountStatus")
		private int accountStatus;
		
		@JsonProperty("avatarImgId")
		private long avatarImgId;
		
		@JsonProperty("defaultAvatar")
		private boolean defaultAvatar;
		
		@JsonProperty("avatarImgIdStr")
		private String avatarImgIdStr;
		
		@JsonProperty("backgroundImgIdStr")
		private String backgroundImgIdStr;
		
		@JsonProperty("province")
		private int province;
		
		@JsonProperty("followMe")
		private boolean followMe;
		
		@JsonProperty("artistIdentity")
		private List<Object> artistIdentity;
		
		@JsonProperty("nickname")
		private String nickname;
		
		@JsonProperty("expertTags")
		private Object expertTags;
		
		@JsonProperty("sDJPCount")
		private int sDJPCount;
		
		@JsonProperty("newFollows")
		private int newFollows;
		
		@JsonProperty("djStatus")
		private int djStatus;
		
		@JsonProperty("avatarUrl")
		private String avatarUrl;
		
		@JsonProperty("authStatus")
		private int authStatus;
		
		@JsonProperty("follows")
		private int follows;
		
		@JsonProperty("vipType")
		private int vipType;
		
		@JsonProperty("blacklist")
		private boolean blacklist;
		
		@JsonProperty("followed")
		private boolean followed;
		
		@JsonProperty("userId")
		private long userId;
		
		@JsonProperty("mutual")
		private boolean mutual;
		
		@JsonProperty("avatarImgId_str")
		private String avatarImgId_Str;
		
		@JsonProperty("followTime")
		private Object followTime;
		
		@JsonProperty("createTime")
		private long createTime;
		
		@JsonProperty("authority")
		private int authority;
		
		@JsonProperty("cCount")
		private int cCount;
		
		@JsonProperty("inBlacklist")
		private boolean inBlacklist;
		
		@JsonProperty("backgroundImgId")
		private long backgroundImgId;
		
		@JsonProperty("userType")
		private int userType;
		
		@JsonProperty("avatarDetail")
		private Object avatarDetail;
		
		@JsonProperty("experts")
		private Experts experts;
		
		@JsonProperty("playlistCount")
		private int playlistCount;
		
		@JsonProperty("sCount")
		private int sCount;
		
		@Getter
		@Setter
		public static class PrivacyItemUnlimit {
			
			@JsonProperty("area")
			private boolean area;
			
			@JsonProperty("college")
			private boolean college;
			
			@JsonProperty("gender")
			private boolean gender;
			
			@JsonProperty("age")
			private boolean age;
			
			@JsonProperty("villageAge")
			private boolean villageAge;
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
		private int expiresIn;
		
		@JsonProperty("expired")
		private boolean expired;
		
		@JsonProperty("tokenJsonStr")
		private Object tokenJsonStr;
		
		@JsonProperty("refreshTime")
		private int refreshTime;
		
		@JsonProperty("id")
		private int id;
		
		@JsonProperty("type")
		private int type;
		
		@JsonProperty("bindingTime")
		private long bindingTime;
		
		@JsonProperty("userId")
		private int userId;
		
		@JsonProperty("url")
		private String url;
	}
	
	
}
