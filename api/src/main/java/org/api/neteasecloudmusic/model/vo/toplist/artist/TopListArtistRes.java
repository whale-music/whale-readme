package org.api.neteasecloudmusic.model.vo.toplist.artist;

import lombok.Data;

import java.util.List;

@Data
public class TopListArtistRes {
    private List<ArtistsItem> artists;
    private long updateTime;
    private int type;
}