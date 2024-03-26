package org.api.nmusic.model.vo.toplist.toplist;

import lombok.Data;

import java.util.List;

@Data
public class ListItem{
	private Integer privacy;
	private String description;
	private Long trackNumberUpdateTime;
	private Object subscribed;
	private Integer adType;
	private Integer trackCount;
	private String coverImgIdStr;
	private Integer specialType;
	private Object artists;
	private Object socialPlaylistCover;
	private Long id;
	private Object englishTitle;
	private Object recommendInfo;
	private Integer totalDuration;
	private Boolean ordered;
	private Object creator;
	private List<Object> subscribers;
	private Object backgroundCoverUrl;
	private Boolean opRecommend;
	private Boolean highQuality;
	private String commentThreadId;
	private Long updateTime;
	private Long trackUpdateTime;
	private Integer userId;
	private Object tracks;
	private Boolean anonimous;
	private List<Object> tags;
	private Integer titleImage;
	private String coverImgUrl;
	private Integer cloudTrackCount;
	private Long playCount;
	private Long coverImgId;
	private Long createTime;
	private String name;
	private Integer backgroundCoverId;
	private Integer subscribedCount;
	private String titleImageUrl;
	private String updateFrequency;
	private Boolean newImported;
	private Integer status;
	private String toplistType;
}