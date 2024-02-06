package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastMusicRes {
    private String musicPic;
    private List<ArtistNameId> artists;
    private String musicName;
    private String musicNameAlias;
    private Long musicId;
    private LocalDate createDate;
    private Integer playCount;
    private Integer numberOfFavorites;
    private Integer loveTheData;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArtistNameId {
        private Long id;
        private String name;
    }
}
