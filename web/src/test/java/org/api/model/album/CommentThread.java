package org.api.model.album;

public class CommentThread{
	private int shareCount;
	private int resourceId;
	private Object latestLikedUsers;
	private int hotCount;
	private String resourceTitle;
	private long resourceOwnerId;
	private String id;
	private int likedCount;
	private ResourceInfo resourceInfo;
	private int resourceType;
	private int commentCount;

	public int getShareCount(){
		return shareCount;
	}

	public void setShareCount(int shareCount){
		this.shareCount = shareCount;
	}

	public int getResourceId(){
		return resourceId;
	}

	public void setResourceId(int resourceId){
		this.resourceId = resourceId;
	}

	public Object getLatestLikedUsers(){
		return latestLikedUsers;
	}

	public void setLatestLikedUsers(Object latestLikedUsers){
		this.latestLikedUsers = latestLikedUsers;
	}

	public int getHotCount(){
		return hotCount;
	}

	public void setHotCount(int hotCount){
		this.hotCount = hotCount;
	}

	public String getResourceTitle(){
		return resourceTitle;
	}

	public void setResourceTitle(String resourceTitle){
		this.resourceTitle = resourceTitle;
	}

	public long getResourceOwnerId(){
		return resourceOwnerId;
	}

	public void setResourceOwnerId(long resourceOwnerId){
		this.resourceOwnerId = resourceOwnerId;
	}

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

	public int getLikedCount(){
		return likedCount;
	}

	public void setLikedCount(int likedCount){
		this.likedCount = likedCount;
	}

	public ResourceInfo getResourceInfo(){
		return resourceInfo;
	}

	public void setResourceInfo(ResourceInfo resourceInfo){
		this.resourceInfo = resourceInfo;
	}

	public int getResourceType(){
		return resourceType;
	}

	public void setResourceType(int resourceType){
		this.resourceType = resourceType;
	}

	public int getCommentCount(){
		return commentCount;
	}

	public void setCommentCount(int commentCount){
		this.commentCount = commentCount;
	}

	@Override
 	public String toString(){
		return 
			"CommentThread{" + 
			"shareCount = '" + shareCount + '\'' + 
			",resourceId = '" + resourceId + '\'' + 
			",latestLikedUsers = '" + latestLikedUsers + '\'' + 
			",hotCount = '" + hotCount + '\'' + 
			",resourceTitle = '" + resourceTitle + '\'' + 
			",resourceOwnerId = '" + resourceOwnerId + '\'' + 
			",id = '" + id + '\'' + 
			",likedCount = '" + likedCount + '\'' + 
			",resourceInfo = '" + resourceInfo + '\'' + 
			",resourceType = '" + resourceType + '\'' + 
			",commentCount = '" + commentCount + '\'' + 
			"}";
		}
}
