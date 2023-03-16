package org.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicDetails {
    private TbMusicPojo music;
    private TbAlbumPojo album;
    private List<TbSingerPojo> singer;
    private TbMusicUrlPojo musicUrl;
}
