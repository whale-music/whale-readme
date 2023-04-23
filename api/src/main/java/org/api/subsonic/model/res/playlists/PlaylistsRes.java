package org.api.subsonic.model.res.playlists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.subsonic.common.SubsonicResult;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PlaylistsRes extends SubsonicResult {
    private Playlists playlists;
}
