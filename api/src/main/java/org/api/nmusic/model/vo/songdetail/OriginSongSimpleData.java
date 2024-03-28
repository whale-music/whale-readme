package org.api.nmusic.model.vo.songdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
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
