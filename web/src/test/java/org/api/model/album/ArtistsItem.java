package org.api.model.album;

import java.util.List;

public class ArtistsItem {
    private String img1v1Url;
    private int musicSize;
    private String img1v1IdStr;
    private long img1v1Id;
    private boolean followed;
    private int albumSize;
    private String picUrl;
    private int topicPerson;
    private String briefDesc;
    private String name;
    private List<Object> alias;
    private int id;
    private int picId;
    private String trans;
    
    public String getImg1v1Url() {
        return img1v1Url;
    }
    
    public void setImg1v1Url(String img1v1Url) {
        this.img1v1Url = img1v1Url;
    }
    
    public int getMusicSize() {
        return musicSize;
    }
    
    public void setMusicSize(int musicSize) {
        this.musicSize = musicSize;
    }
    
    public String getImg1v1IdStr() {
        return img1v1IdStr;
    }
    
    public void setImg1v1IdStr(String img1v1IdStr) {
        this.img1v1IdStr = img1v1IdStr;
    }
    
    public long getImg1v1Id() {
        return img1v1Id;
    }
    
    public void setImg1v1Id(long img1v1Id) {
        this.img1v1Id = img1v1Id;
    }
    
    public boolean isFollowed() {
        return followed;
    }
    
    public void setFollowed(boolean followed) {
        this.followed = followed;
    }
    
    public int getAlbumSize() {
        return albumSize;
    }
    
    public void setAlbumSize(int albumSize) {
        this.albumSize = albumSize;
    }
    
    public String getPicUrl() {
        return picUrl;
    }
    
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
    
    public int getTopicPerson() {
        return topicPerson;
    }
    
    public void setTopicPerson(int topicPerson) {
        this.topicPerson = topicPerson;
    }
    
    public String getBriefDesc() {
        return briefDesc;
    }
    
    public void setBriefDesc(String briefDesc) {
        this.briefDesc = briefDesc;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<Object> getAlias() {
        return alias;
    }
    
    public void setAlias(List<Object> alias) {
        this.alias = alias;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getPicId() {
        return picId;
    }
    
    public void setPicId(int picId) {
        this.picId = picId;
    }
    
    public String getTrans() {
        return trans;
    }
    
    public void setTrans(String trans) {
        this.trans = trans;
    }
    
    @Override
    public String toString() {
        return
                "ArtistsItem{" +
                        "img1v1Url = '" + img1v1Url + '\'' +
                        ",musicSize = '" + musicSize + '\'' +
                        ",img1v1Id_str = '" + img1v1IdStr + '\'' +
                        ",img1v1Id = '" + img1v1Id + '\'' +
                        ",followed = '" + followed + '\'' +
                        ",albumSize = '" + albumSize + '\'' +
                        ",picUrl = '" + picUrl + '\'' +
                        ",topicPerson = '" + topicPerson + '\'' +
                        ",briefDesc = '" + briefDesc + '\'' +
                        ",name = '" + name + '\'' +
                        ",alias = '" + alias + '\'' +
                        ",id = '" + id + '\'' +
                        ",picId = '" + picId + '\'' +
                        ",trans = '" + trans + '\'' +
                        "}";
    }
}