package org.api.model.singer;

import java.util.List;

public class Artist {
    private String cover;
    private List<String> identities;
    private String briefDesc;
    private List<String> transNames;
    private int musicSize;
    private String name;
    private List<String> alias;
    private Object rank;
    private int id;
    private int mvSize;
    private Object identifyTag;
    private int albumSize;
    
    public String getCover() {
        return cover;
    }
    
    public void setCover(String cover) {
        this.cover = cover;
    }
    
    public List<String> getIdentities() {
        return identities;
    }
    
    public void setIdentities(List<String> identities) {
        this.identities = identities;
    }
    
    public String getBriefDesc() {
        return briefDesc;
    }
    
    public void setBriefDesc(String briefDesc) {
        this.briefDesc = briefDesc;
    }
    
    public List<String> getTransNames() {
        return transNames;
    }
    
    public void setTransNames(List<String> transNames) {
        this.transNames = transNames;
    }
    
    public int getMusicSize() {
        return musicSize;
    }
    
    public void setMusicSize(int musicSize) {
        this.musicSize = musicSize;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<String> getAlias() {
        return alias;
    }
    
    public void setAlias(List<String> alias) {
        this.alias = alias;
    }
    
    public Object getRank() {
        return rank;
    }
    
    public void setRank(Object rank) {
        this.rank = rank;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getMvSize() {
        return mvSize;
    }
    
    public void setMvSize(int mvSize) {
        this.mvSize = mvSize;
    }
    
    public Object getIdentifyTag() {
        return identifyTag;
    }
    
    public void setIdentifyTag(Object identifyTag) {
        this.identifyTag = identifyTag;
    }
    
    public int getAlbumSize() {
        return albumSize;
    }
    
    public void setAlbumSize(int albumSize) {
        this.albumSize = albumSize;
    }
    
    @Override
    public String toString() {
        return
                "Artist{" +
                        "cover = '" + cover + '\'' +
                        ",identities = '" + identities + '\'' +
                        ",briefDesc = '" + briefDesc + '\'' +
                        ",transNames = '" + transNames + '\'' +
                        ",musicSize = '" + musicSize + '\'' +
                        ",name = '" + name + '\'' +
                        ",alias = '" + alias + '\'' +
                        ",rank = '" + rank + '\'' +
                        ",id = '" + id + '\'' +
                        ",mvSize = '" + mvSize + '\'' +
                        ",identifyTag = '" + identifyTag + '\'' +
                        ",albumSize = '" + albumSize + '\'' +
                        "}";
    }
}