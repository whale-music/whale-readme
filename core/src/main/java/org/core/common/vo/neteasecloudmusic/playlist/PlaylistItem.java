package org.core.common.vo.neteasecloudmusic.playlist;

import lombok.Data;

import java.util.List;

@Data
public class PlaylistItem {
	private Object sharedUsers;
	private boolean copied;
	private Integer privacy;
	private String description;
	private Long trackNumberUpdateTime;
	private boolean subscribed;
	private Integer trackCount;
	private Integer adType;
	private String coverImgIdStr;
	private Integer specialType;
	private Object artists;
	private Long id;
	private Object englishTitle;
	private Object recommendInfo;
	private Integer totalDuration;
	private boolean ordered;
	private Creator creator;
	private List<Object> subscribers;
	private Object backgroundCoverUrl;
	private boolean opRecommend;
	private String commentThreadId;
	private boolean highQuality;
	private Long updateTime;
	private Long trackUpdateTime;
	private Long userId;
	private Object tracks;
	private boolean anonimous;
	private List<String> tags;
	private Integer titleImage;
	private Integer cloudTrackCount;
	private String coverImgUrl;
	private Integer playCount;
	private Long coverImgId;
	private Integer createTime;
	private String name;
	private Integer backgroundCoverId;
	private Integer subscribedCount;
	private Object titleImageUrl;
	private Object updateFrequency;
	private boolean newImported;
	private Integer status;
	private Object shareStatus;
}