package org.api.model.playlist;

import lombok.Data;

import java.util.List;

@Data
public class PlayList {
    private List<PrivilegesItem> privileges;
    private int code;
    private List<SongsItem> songs;
}