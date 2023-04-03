package org.api.model.album;

public class ResourceInfo {
    private String imgUrl;
    private Object creator;
    private Object subTitle;
    private Object webUrl;
    private String name;
    private int id;
    private long userId;
    private Object encodedId;
    
    public String getImgUrl() {
        return imgUrl;
    }
    
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    
    public Object getCreator() {
        return creator;
    }
    
    public void setCreator(Object creator) {
        this.creator = creator;
    }
    
    public Object getSubTitle() {
        return subTitle;
    }
    
    public void setSubTitle(Object subTitle) {
        this.subTitle = subTitle;
    }
    
    public Object getWebUrl() {
        return webUrl;
    }
    
    public void setWebUrl(Object webUrl) {
        this.webUrl = webUrl;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public long getUserId() {
        return userId;
    }
    
    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    public Object getEncodedId() {
        return encodedId;
    }
    
    public void setEncodedId(Object encodedId) {
        this.encodedId = encodedId;
    }
    
    @Override
    public String toString() {
        return
                "ResourceInfo{" +
                        "imgUrl = '" + imgUrl + '\'' +
                        ",creator = '" + creator + '\'' +
                        ",subTitle = '" + subTitle + '\'' +
                        ",webUrl = '" + webUrl + '\'' +
                        ",name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",userId = '" + userId + '\'' +
                        ",encodedId = '" + encodedId + '\'' +
                        "}";
    }
}
