package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.pojo.TbAlbumPojo;
import org.core.pojo.TbMusicPojo;
import org.core.pojo.TbSingerPojo;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PlayListMusicRes extends TbMusicPojo {
    private TbAlbumPojo album;
    private List<TbSingerPojo> singers;
}
