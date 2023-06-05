package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.model.convert.ArtistConvert;
import org.core.model.convert.MusicConvert;
import org.core.pojo.TbAlbumPojo;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PlayListMusicRes extends MusicConvert {
    private TbAlbumPojo album;
    private List<ArtistConvert> artists;
}
