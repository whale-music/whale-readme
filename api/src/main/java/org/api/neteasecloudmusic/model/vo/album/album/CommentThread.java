package org.api.neteasecloudmusic.model.vo.album.album;

import lombok.Data;

@Data
public class CommentThread {
    private int shareCount;
    private int resourceId;
    private Object latestLikedUsers;
    private int hotCount;
    private String resourceTitle;
    private int resourceOwnerId;
    private String id;
    private int likedCount;
    private ResourceInfo resourceInfo;
    private int resourceType;
    private int commentCount;
}