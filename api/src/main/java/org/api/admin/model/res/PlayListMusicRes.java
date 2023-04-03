package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.pojo.AlbumPojo;
import org.core.pojo.ArtistPojo;
import org.core.pojo.MusicPojo;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PlayListMusicRes extends MusicPojo {
    private AlbumPojo album;
    private List<ArtistPojo> singers;
}
