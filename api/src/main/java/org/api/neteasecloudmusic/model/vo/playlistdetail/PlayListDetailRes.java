package org.api.neteasecloudmusic.model.vo.playlistdetail;

import lombok.Data;

import java.util.List;

@Data
public class PlayListDetailRes {
    private List<PrivilegesItem> privileges;
    private Object urls;
    private Object resEntrance;
    private Integer code;
    private Playlist playlist;
    private Integer fromUserCount;
    private Object relatedVideos;
    private Object songFromUsers;
    private Object sharedPrivilege;
    private Object fromUsers;
}