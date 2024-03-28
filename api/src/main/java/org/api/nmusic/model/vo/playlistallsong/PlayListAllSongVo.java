package org.api.nmusic.model.vo.playlistallsong;

import lombok.Data;

import java.util.List;

@Data
public class PlayListAllSongVo {
    private List<PrivilegesItem> privileges;
    private Integer code;
    private List<SongsItem> songs;
}