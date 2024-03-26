package org.api.nmusic.model.vo.songdetail;

import lombok.Data;

import java.util.List;

@Data
public class SongDetailRes {
    private List<PrivilegesItem> privileges;
    private Integer code;
    private List<SongsItem> songs;
}