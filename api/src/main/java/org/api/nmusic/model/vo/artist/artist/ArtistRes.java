package org.api.nmusic.model.vo.artist.artist;

import lombok.Data;

import java.util.List;

@Data
public class ArtistRes {
    private int code;
    private List<HotSongsItem> hotSongs;
    private Artist artist;
    private boolean more;
}