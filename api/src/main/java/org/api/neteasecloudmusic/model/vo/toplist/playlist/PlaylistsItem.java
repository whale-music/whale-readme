package org.api.neteasecloudmusic.model.vo.toplist.playlist;

import lombok.Data;

import java.util.List;

@Data
public class PlaylistsItem {
    private String description;
    private int privacy;
    private long trackNumberUpdateTime;
    private boolean subscribed;
    private int shareCount;
    private int trackCount;
    private int adType;
    private String coverImgIdStr;
    private int specialType;
    private Long id;
    private Object socialPlaylistCover;
    private String alg;
    private int totalDuration;
    private Object recommendInfo;
    private boolean ordered;
    private Creator creator;
    private List<SubscribersItem> subscribers;
    private Object recommendText;
    private String commentThreadId;
    private boolean highQuality;
    private long updateTime;
    private long trackUpdateTime;
    private Long userId;
    private int coverStatus;
    private Object tracks;
    private List<String> tags;
    private boolean anonimous;
    private int commentCount;
    private int cloudTrackCount;
    private String coverImgUrl;
    private int playCount;
    private long coverImgId;
    private long createTime;
    private String name;
    private int subscribedCount;
    private int status;
    private boolean newImported;
}