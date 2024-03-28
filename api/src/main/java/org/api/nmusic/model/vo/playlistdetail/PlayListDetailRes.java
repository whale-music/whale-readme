package org.api.nmusic.model.vo.playlistdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
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
	
	
	@Getter
	@Setter
	public static class PrivilegesItem {
		
		@JsonProperty("flag")
		private Integer flag;
		
		@JsonProperty("dlLevel")
		private String dlLevel;
		
		@JsonProperty("subp")
		private Integer subp;
		
		@JsonProperty("fl")
		private Integer fl;
		
		@Schema(name = "0: 免费或无版权, 1: VIP 歌曲, 4: 购买专辑, 8: 非会员可免费播放低音质，会员可播放高音质及下载", description = "fee 为 1 或 8 的歌曲均可单独购买 2 元单曲")
		@JsonProperty("fee")
		private Integer fee;
		
		@JsonProperty("dl")
		private Integer dl;
		
		@JsonProperty("plLevel")
		private String plLevel;
		
		@JsonProperty("paidBigBang")
		private Boolean paidBigBang;
		
		@JsonProperty("maxBrLevel")
		private String maxBrLevel;
		
		@JsonProperty("rightSource")
		private Integer rightSource;
		
		@JsonProperty("maxbr")
		private Integer maxbr;
		
		@JsonProperty("id")
		private Long id;
		
		@JsonProperty("sp")
		private Integer sp;
		
		@JsonProperty("payed")
		private Integer payed;
		
		@JsonProperty("rscl")
		private Object rscl;
		
		@JsonProperty("st")
		private Integer st;
		
		@JsonProperty("realPayed")
		private Integer realPayed;
		
		@JsonProperty("chargeInfoList")
		private List<ChargeInfoListItem> chargeInfoList;
		
		@JsonProperty("freeTrialPrivilege")
		private FreeTrialPrivilege freeTrialPrivilege;
		
		@JsonProperty("downloadMaxbr")
		private Integer downloadMaxbr;
		
		@JsonProperty("downloadMaxBrLevel")
		private String downloadMaxBrLevel;
		
		@JsonProperty("cp")
		private Integer cp;
		
		@JsonProperty("preSell")
		private Boolean preSell;
		
		@JsonProperty("playMaxBrLevel")
		private String playMaxBrLevel;
		
		@JsonProperty("cs")
		private Boolean cs;
		
		@JsonProperty("toast")
		private Boolean toast;
		
		@JsonProperty("playMaxbr")
		private Integer playMaxbr;
		
		@JsonProperty("pc")
		private Object pc;
		
		@JsonProperty("flLevel")
		private String flLevel;
		
		@JsonProperty("pl")
		private Integer pl;
		
		@Getter
		@Setter
		public static class FreeTrialPrivilege {
			
			@JsonProperty("userConsumable")
			private Boolean userConsumable;
			
			@JsonProperty("resConsumable")
			private Boolean resConsumable;
			
			@JsonProperty("cannotListenReason")
			private Integer cannotListenReason;
			
			@JsonProperty("playReason")
			private Object playReason;
			
			@JsonProperty("listenType")
			private Integer listenType;
		}
		
		@Getter
		@Setter
		@AllArgsConstructor
		public static class ChargeInfoListItem {
			
			@JsonProperty("rate")
			private Integer rate;
			
			@JsonProperty("chargeMessage")
			private Object chargeMessage;
			
			@JsonProperty("chargeType")
			private Integer chargeType;
			
			@JsonProperty("chargeUrl")
			private Object chargeUrl;
		}
		
	}
	
	@Getter
	@Setter
	public static class Playlist {
		
		@JsonProperty("sharedUsers")
		private Object sharedUsers;
		
		@JsonProperty("copied")
		private Boolean copied;
		
		@JsonProperty("historySharedUsers")
		private Object historySharedUsers;
		
		@JsonProperty("remixVideo")
		private Object remixVideo;
		
		@JsonProperty("videos")
		private Object videos;
		
		@JsonProperty("gradeStatus")
		private String gradeStatus;
		
		@JsonProperty("trialMode")
		private Integer trialMode;
		
		@JsonProperty("trackNumberUpdateTime")
		private Long trackNumberUpdateTime;
		
		@JsonProperty("algTags")
		private Object algTags;
		
		@JsonProperty("subscribed")
		private Boolean subscribed;
		
		@JsonProperty("shareCount")
		private Integer shareCount;
		
		@JsonProperty("score")
		private Object score;
		
		@JsonProperty("coverImgId_str")
		private String coverImgIdStr;
		
		@JsonProperty("trackCount")
		private Integer trackCount;
		
		@JsonProperty("specialType")
		private Integer specialType;
		
		@JsonProperty("id")
		private Long id;
		
		@JsonProperty("videoIds")
		private Object videoIds;
		
		@JsonProperty("englishTitle")
		private Object englishTitle;
		
		@JsonProperty("mvResourceInfos")
		private Object mvResourceInfos;
		
		@JsonProperty("subscribers")
		private List<SubscribersItem> subscribers;
		
		@JsonProperty("backgroundCoverUrl")
		private Object backgroundCoverUrl;
		
		@JsonProperty("commentThreadId")
		private String commentThreadId;
		
		@JsonProperty("trackUpdateTime")
		private Long trackUpdateTime;
		
		@JsonProperty("tags")
		private List<Object> tags;
		
		@JsonProperty("commentCount")
		private Integer commentCount;
		
		@JsonProperty("coverImgUrl")
		private String coverImgUrl;
		
		@JsonProperty("cloudTrackCount")
		private Integer cloudTrackCount;
		
		@JsonProperty("playCount")
		private Integer playCount;
		
		@JsonProperty("coverImgId")
		private Long coverImgId;
		
		@JsonProperty("name")
		private String name;
		
		@JsonProperty("backgroundCoverId")
		private Integer backgroundCoverId;
		
		@JsonProperty("updateFrequency")
		private Object updateFrequency;
		
		@JsonProperty("status")
		private Integer status;
		
		@JsonProperty("officialPlaylistType")
		private Object officialPlaylistType;
		
		@JsonProperty("bannedTrackIds")
		private Object bannedTrackIds;
		
		@JsonProperty("privacy")
		private Integer privacy;
		
		@JsonProperty("description")
		private Object description;
		
		@JsonProperty("relateResType")
		private Object relateResType;
		
		@JsonProperty("adType")
		private Integer adType;
		
		@JsonProperty("trackIds")
		private List<TrackIdsItem> trackIds;
		
		@JsonProperty("ordered")
		private Boolean ordered;
		
		@JsonProperty("creator")
		private Creator creator;
		
		@JsonProperty("opRecommend")
		private Boolean opRecommend;
		
		@JsonProperty("highQuality")
		private Boolean highQuality;
		
		@JsonProperty("updateTime")
		private Long updateTime;
		
		@JsonProperty("userId")
		private Long userId;
		
		@JsonProperty("tracks")
		private List<TracksItem> tracks;
		
		@JsonProperty("titleImage")
		private Integer titleImage;
		
		@JsonProperty("createTime")
		private Long createTime;
		
		@JsonProperty("subscribedCount")
		private Integer subscribedCount;
		
		@JsonProperty("titleImageUrl")
		private Object titleImageUrl;
		
		@JsonProperty("newImported")
		private Boolean newImported;
		
		@Getter
		@Setter
		public static class Creator {
			
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
		
		@Getter
		@Setter
		public static class TrackIdsItem {
			
			@JsonProperty("sc")
			private Object sc;
			
			@JsonProperty("uid")
			private Long uid;
			
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
		
		
		@Getter
		@Setter
		public static class TracksItem {
			
			@JsonProperty("no")
			private Integer no;
			
			@JsonProperty("rt")
			private String rt;
			
			@JsonProperty("copyright")
			private Integer copyright;
			
			@JsonProperty("fee")
			private Integer fee;
			
			@JsonProperty("rurl")
			private Object rurl;
			
			@JsonProperty("hr")
			private Object hr;
			
			@JsonProperty("tagPicList")
			private Object tagPicList;
			
			@JsonProperty("mst")
			private Integer mst;
			
			@JsonProperty("pst")
			private Integer pst;
			
			@JsonProperty("pop")
			private Integer pop;
			
			@Schema(name = "歌曲时长")
			@JsonProperty("dt")
			private Integer dt;
			
			@JsonProperty("awardTags")
			private Object awardTags;
			
			@JsonProperty("rtype")
			private Integer rtype;
			
			@JsonProperty("s_id")
			private Integer sId;
			
			@JsonProperty("rtUrls")
			private List<Object> rtUrls;
			
			@JsonProperty("resourceState")
			private Boolean resourceState;
			
			@JsonProperty("songJumpInfo")
			private Object songJumpInfo;
			
			@JsonProperty("id")
			private Long id;
			
			@JsonProperty("entertainmentTags")
			private Object entertainmentTags;
			
			@JsonProperty("sq")
			private Sq sq;
			
			@JsonProperty("st")
			private Integer st;
			
			@JsonProperty("a")
			private Object a;
			
			@JsonProperty("cd")
			private String cd;
			
			@JsonProperty("publishTime")
			private Long publishTime;
			
			@JsonProperty("cf")
			private String cf;
			
			@JsonProperty("originCoverType")
			private Integer originCoverType;
			
			@JsonProperty("h")
			private H h;
			
			@JsonProperty("mv")
			private Integer mv;
			
			@JsonProperty("al")
			private Al al;
			
			@JsonProperty("originSongSimpleData")
			private OriginSongSimpleData originSongSimpleData;
			
			@JsonProperty("l")
			private L l;
			
			@JsonProperty("m")
			private M m;
			
			@JsonProperty("version")
			private Integer version;
			
			@JsonProperty("cp")
			private Integer cp;
			
			@JsonProperty("alia")
			private List<String> alia;
			
			@JsonProperty("djId")
			private Integer djId;
			
			@JsonProperty("noCopyrightRcmd")
			private Object noCopyrightRcmd;
			
			@JsonProperty("crbt")
			private Object crbt;
			
			@JsonProperty("single")
			private Integer single;
			
			@JsonProperty("ar")
			private List<ArItem> ar;
			
			@JsonProperty("rtUrl")
			private Object rtUrl;
			
			@JsonProperty("ftype")
			private Integer ftype;
			
			@JsonProperty("t")
			private Integer t;
			
			@JsonProperty("v")
			private Integer v;
			
			@JsonProperty("name")
			private String name;
			
			@JsonProperty("tns")
			private List<String> tns;
			
			@JsonProperty("mark")
			private Integer mark;
			
			@Getter
			@Setter
			public static class OriginSongSimpleData {
				
				@JsonProperty("artists")
				private List<ArtistsItem> artists;
				
				@JsonProperty("name")
				private String name;
				
				@JsonProperty("songId")
				private Integer songId;
				
				@JsonProperty("albumMeta")
				private AlbumMeta albumMeta;
				
				@Getter
				@Setter
				public static class ArtistsItem {
					
					@JsonProperty("name")
					private String name;
					
					@JsonProperty("id")
					private Integer id;
				}
				
				@Getter
				@Setter
				public static class AlbumMeta {
					
					@JsonProperty("name")
					private String name;
					
					@JsonProperty("id")
					private Integer id;
				}
				
				
			}
			
			@Getter
			@Setter
			public static class Sq {
				
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
			
			
			@Getter
			@Setter
			public static class M {
				
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
			
			
			@Getter
			@Setter
			public static class L {
				
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
			
			@Getter
			@Setter
			public static class Hr {
				
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
			
			
			@Getter
			@Setter
			public static class H {
				
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
			
			
			@Getter
			@Setter
			public static class ArItem {
				
				@JsonProperty("name")
				private String name;
				
				@JsonProperty("tns")
				private List<Object> tns;
				
				@JsonProperty("alias")
				private List<String> alias;
				
				@JsonProperty("id")
				private Long id;
			}
			
			
			@Getter
			@Setter
			public static class Al {
				
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
			
		}
		
		
		@Getter
		@Setter
		public static class SubscribersItem {
			
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
			
			@JsonProperty("avatarImgId_str")
			private String avatarImgId_Str;
			
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
		
	}
	
}
