package org.api.subsonic.model.res.albumlist2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumList2 {
    
    @JsonProperty("album")
    private List<AlbumItem> album;
}