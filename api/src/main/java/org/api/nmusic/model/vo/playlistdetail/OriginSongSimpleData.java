package org.api.nmusic.model.vo.playlistdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OriginSongSimpleData {
    
    @JsonProperty("artists")
    private List<ArtistsItem> artists;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("songId")
    private Integer songId;
    
    @JsonProperty("albumMeta")
    private AlbumMeta albumMeta;
}
