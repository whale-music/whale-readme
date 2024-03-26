package org.api.nmusic.model.vo.createplatlist;

import lombok.Data;

import java.util.List;

@Data
public class Playlist {
    private Object sharedUsers;
    private Boolean copied;
    private Integer privacy;
    private Object description;
    private Integer trackNumberUpdateTime;
    private Object subscribed;
    private Integer trackCount;
    private Integer adType;
    private String coverImgIdStr;
    private Integer specialType;
    private Object artists;
    private Long id;
    private Object englishTitle;
    private Object recommendInfo;
    private Integer totalDuration;
    private Boolean ordered;
    private Object creator;
    private List<Object> subscribers;
    private Object backgroundCoverUrl;
    private Boolean opRecommend;
    private String commentThreadId;
    private Boolean highQuality;
    private Integer updateTime;
    private Integer trackUpdateTime;
    private Long userId;
    private Object tracks;
    private Boolean anonimous;
    private List<Object> tags;
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
    private Boolean newImported;
    private Integer status;
    private Object shareStatus;
}