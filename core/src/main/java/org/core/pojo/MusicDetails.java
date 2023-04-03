package org.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicDetails {
    private MusicPojo music;
    private AlbumPojo album;
    private List<ArtistPojo> singer;
    private MusicUrlPojo musicUrl;
}
