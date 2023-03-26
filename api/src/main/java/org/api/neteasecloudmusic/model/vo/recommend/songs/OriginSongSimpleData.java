package org.api.neteasecloudmusic.model.vo.recommend.songs;

import lombok.Data;

import java.util.List;

@Data
public class OriginSongSimpleData {
    private List<ArtistsItem> artists;
    private String name;
    private Integer songId;
    private AlbumMeta albumMeta;
}