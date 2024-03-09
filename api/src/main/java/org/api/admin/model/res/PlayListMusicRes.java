package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.model.convert.MusicConvert;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PlayListMusicRes extends MusicConvert {
    private PlayListAlbum album;
    private List<PlayListArtist> artists;
    
    @Data
    public static class PlayListAlbum implements Serializable {
        public PlayListAlbum(Long id, String albumName) {
            this.id = id;
            this.albumName = albumName;
        }
        
        private Long id;
        private String albumName;
    }
    
    @Data
    public static class PlayListArtist implements Serializable {
        private Long id;
        private String artistName;
        private String aliasName;
        public PlayListArtist(Long id, String artistName, String aliasName) {
            this.id = id;
            this.artistName = artistName;
            this.aliasName = aliasName;
        }
    }
    
}
