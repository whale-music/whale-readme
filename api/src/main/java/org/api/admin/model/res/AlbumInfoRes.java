package org.api.admin.model.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.MusicConvert;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AlbumInfoRes extends AlbumConvert {
    @Schema(name = "专辑流派")
    private List<String> albumGenre;
    
    @Schema(name = "音乐数据")
    private List<AlbumMusic> musicList;
    
    @Schema(name = "专辑歌曲数量")
    private Long albumSize;
    
    @Schema(name = "歌手信息")
    private List<AlbumArtist> artistList;
    
    public void setMusicList(Collection<MusicConvert> musicList) {
        this.musicList = musicList.parallelStream().map(AlbumMusic::new).toList();
    }
    
    public void setArtistList(Collection<ArtistConvert> artistList) {
        this.artistList = artistList.parallelStream().map(AlbumArtist::new).toList();
    }
    
    @Data
    public static class AlbumMusic implements Serializable {
        public AlbumMusic(MusicConvert m) {
            this.id = m.getId();
            this.musicName = m.getMusicName();
            this.aliasName = m.getAliasName();
            this.timeLength = m.getTimeLength();
        }
        
        private Long id;
        private String musicName;
        private String aliasName;
        private Integer timeLength;
    }
    
    @Data
    public static class AlbumArtist implements Serializable {
        public AlbumArtist(ArtistConvert a) {
            this.id = a.getId();
            this.artistName = a.getArtistName();
            this.aliasName = a.getAliasName();
        }
        
        @Schema(title = "歌手ID")
        private Long id;
        
        @Schema(title = "歌手名")
        private String artistName;
        
        @Schema(title = "歌手别名")
        private String aliasName;
    }
}
