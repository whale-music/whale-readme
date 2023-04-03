package org.api.model.album;

import java.util.List;

public class AlbumRes {
    private int code;
    private List<SongsItem> songs;
    private Album album;
    private boolean resourceState;
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public List<SongsItem> getSongs() {
        return songs;
    }
    
    public void setSongs(List<SongsItem> songs) {
        this.songs = songs;
    }
    
    public Album getAlbum() {
        return album;
    }
    
    public void setAlbum(Album album) {
        this.album = album;
    }
    
    public boolean isResourceState() {
        return resourceState;
    }
    
    public void setResourceState(boolean resourceState) {
        this.resourceState = resourceState;
    }
    
    @Override
    public String toString() {
        return
                "AlbumRes{" +
                        "code = '" + code + '\'' +
                        ",songs = '" + songs + '\'' +
                        ",album = '" + album + '\'' +
                        ",resourceState = '" + resourceState + '\'' +
                        "}";
    }
}