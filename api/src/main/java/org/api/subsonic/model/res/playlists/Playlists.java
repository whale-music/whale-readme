package org.api.subsonic.model.res.playlists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Playlists {
    
    private List<Playlist> playlist;
}
