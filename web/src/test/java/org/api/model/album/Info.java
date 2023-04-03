package org.api.model.album;

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
    
    public String getThreadId() {
        return threadId;
    }
    
    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }
    
    public int getShareCount() {
        return shareCount;
    }
    
    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }
    
    public int getResourceId() {
        return resourceId;
    }
    
    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
    
    public Object getComments() {
        return comments;
    }
    
    public void setComments(Object comments) {
        this.comments = comments;
    }
    
    public Object getLatestLikedUsers() {
        return latestLikedUsers;
    }
    
    public void setLatestLikedUsers(Object latestLikedUsers) {
        this.latestLikedUsers = latestLikedUsers;
    }
    
    public CommentThread getCommentThread() {
        return commentThread;
    }
    
    public void setCommentThread(CommentThread commentThread) {
        this.commentThread = commentThread;
    }
    
    public int getLikedCount() {
        return likedCount;
    }
    
    public void setLikedCount(int likedCount) {
        this.likedCount = likedCount;
    }
    
    public boolean isLiked() {
        return liked;
    }
    
    public void setLiked(boolean liked) {
        this.liked = liked;
    }
    
    public int getResourceType() {
        return resourceType;
    }
    
    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }
    
    public int getCommentCount() {
        return commentCount;
    }
    
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
    
    @Override
    public String toString() {
        return
                "Info{" +
                        "threadId = '" + threadId + '\'' +
                        ",shareCount = '" + shareCount + '\'' +
                        ",resourceId = '" + resourceId + '\'' +
                        ",comments = '" + comments + '\'' +
                        ",latestLikedUsers = '" + latestLikedUsers + '\'' +
                        ",commentThread = '" + commentThread + '\'' +
                        ",likedCount = '" + likedCount + '\'' +
                        ",liked = '" + liked + '\'' +
                        ",resourceType = '" + resourceType + '\'' +
                        ",commentCount = '" + commentCount + '\'' +
                        "}";
    }
}
