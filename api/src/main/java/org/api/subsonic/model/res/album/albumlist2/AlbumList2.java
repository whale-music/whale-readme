package org.api.subsonic.model.res.album.albumlist2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumList2 {
    
    private List<Album> album;
}
