package org.api.nmusic.model.vo.playlistdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Playlist {
	
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
}
