package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbLyricPojo;
import org.core.mybatis.pojo.TbResourcePojo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicPlayInfoRes {
    private Long id;
    private String musicName;
    private String aliasName;
    private String picUrl;
    private Integer timeLength;
    private Long userId;
    private LocalDateTime publishTime;
    private MusicPlayLyrics lyrics;
    private MusicPlayAlbum album;
    private List<MusicPlayArtist> artists;
    private List<MusicPlaySources> sources;
    
    
    @Data
    @AllArgsConstructor
    public static class MusicPlayArtist {
        private Long id;
        private String artistName;
        private String aliasName;
    }
    
    
    @Data
    public static class MusicPlayAlbum {
        private Long id;
        private String albumName;
    }
    
    @Data
    public static class PlayLyrics {
        public PlayLyrics(Long id, Long musicId, String type, String lyric) {
            this.id = id;
            this.musicId = musicId;
            this.type = type;
            this.lyric = lyric;
        }
        
        public PlayLyrics(TbLyricPojo p) {
            this(p.getId(), p.getMusicId(), p.getType(), p.getLyric());
        }
        
        private Long id;
        private Long musicId;
        private String type;
        private String lyric;
    }
    
    @Data
    public static class MusicPlayLyrics {
        private PlayLyrics lyrics;
        private PlayLyrics kLyrics;
        
        public void setLyrics(TbLyricPojo lyrics) {
            if (Objects.nonNull(lyrics)) {
                this.lyrics = new PlayLyrics(lyrics);
            }
        }
        
        public void setKLyrics(TbLyricPojo kLyrics) {
            if (Objects.nonNull(kLyrics)) {
                this.kLyrics = new PlayLyrics(kLyrics);
            }
        }
    }
    
    @Data
    public static class MusicPlaySources {
        private Long id;
        private Long musicId;
        private Integer rate;
        private String level;
        private String path;
        private String md5;
        private String encodeType;
        private Long size;
        private Long userId;
        private String url;
        public MusicPlaySources(TbResourcePojo source) {
            this(source.getId(),
                    source.getMusicId(),
                    source.getRate(),
                    source.getPath(),
                    source.getMd5(),
                    source.getLevel(),
                    source.getEncodeType(),
                    source.getSize(),
                    source.getUserId());
        }
        public MusicPlaySources(Long id, Long musicId, Integer rate, String path, String md5, String level, String encodeType, Long size, Long userId) {
            this.id = id;
            this.musicId = musicId;
            this.rate = rate;
            this.path = path;
            this.md5 = md5;
            this.level = level;
            this.encodeType = encodeType;
            this.size = size;
            this.userId = userId;
        }
    }
    
}
