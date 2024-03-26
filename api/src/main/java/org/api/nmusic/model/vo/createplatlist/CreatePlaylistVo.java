package org.api.nmusic.model.vo.createplatlist;

import lombok.Data;

@Data
public class CreatePlaylistVo {
    private int code;
    private Playlist playlist;
    private long id;
}