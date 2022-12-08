package org.api.neteasecloudmusic.model.vo.playlistallsong;

import lombok.Data;

import java.util.List;

@Data
public class PlayListAllSongVo {
    private List<PrivilegesItem> privileges;
    private int code;
    private List<SongsItem> songs;
}