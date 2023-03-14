package org.api.neteasecloudmusic.model.vo.playlistdetail;

import lombok.Data;

import java.util.List;

@Data
public class Playlist {
    private Object sharedUsers;
    private boolean copied;
    private Object historySharedUsers;
    private Object remixVideo;
    private Object videos;
    private String gradeStatus;
    private Long trackNumberUpdateTime;
    private Object algTags;
    private boolean subscribed;
    private Integer shareCount;
    private Object score;
    private String coverImgIdStr;
    private Integer trackCount;
    private Integer specialType;
    private Long id;
    private Object videoIds;
    private Object englishTitle;
    private Object mvResourceInfos;
    private List<SubscribersItem> subscribers;
    private Object backgroundCoverUrl;
    private String commentThreadId;
    private Long trackUpdateTime;
    private List<String> tags;
    private Integer commentCount;
    private String coverImgUrl;
    private Integer cloudTrackCount;
    private Integer playCount;
    private Long coverImgId;
    private String name;
    private Integer backgroundCoverId;
    private Object updateFrequency;
    private Integer status;
    private Object officialPlaylistType;
    private Object bannedTrackIds;
    private Integer privacy;
    private String description;
    private Object relateResType;
    private Integer adType;
    private List<TrackIdsItem> trackIds;
    private boolean ordered;
    private Creator creator;
    private boolean opRecommend;
    private boolean highQuality;
    private Long updateTime;
    private Integer userId;
    private List<TracksItem> tracks;
    private Integer titleImage;
    private Long createTime;
    private Integer subscribedCount;
    private Object titleImageUrl;
    private boolean newImported;
}