package org.api.model.song;

import java.util.List;

public class SongDetail {
    private List<PrivilegesItem> privileges;
    private Integer code;
    private List<SongsItem> songs;
    
    public List<PrivilegesItem> getPrivileges() {
        return privileges;
    }
    
    public void setPrivileges(List<PrivilegesItem> privileges) {
        this.privileges = privileges;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    public List<SongsItem> getSongs() {
        return songs;
    }
    
    public void setSongs(List<SongsItem> songs) {
        this.songs = songs;
    }
    
    @Override
    public String toString() {
        return
                "SongDetail{" +
                        "privileges = '" + privileges + '\'' +
                        ",code = '" + code + '\'' +
                        ",songs = '" + songs + '\'' +
                        "}";
    }
}