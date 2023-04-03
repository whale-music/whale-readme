package org.api.neteasecloudmusic.model.vo.album.album;

import lombok.Data;

@Data
public class Info {
    private String threadId;
    private int shareCount;
    private int resourceId;
    private Object comments;
    private Object latestLikedUsers;
    private CommentThread commentThread;
    private int likedCount;
    private boolean liked;
    private int resourceType;
    private int commentCount;
}