package org.api.admin.model.res;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbLyricPojo;
import org.core.mybatis.pojo.TbResourcePojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class MusicInfoRes implements Serializable {
    
    @ApiModelProperty("音乐ID")
    private Long id;
    
    @ApiModelProperty("音乐名")
    private String musicName;
    
    @ApiModelProperty("音乐别名")
    private String aliasName;
    
    @ApiModelProperty("音乐tag")
    private List<String> musicTag;
    
    @ApiModelProperty("音乐流派")
    private List<String> musicGenre;
    
    @ApiModelProperty("封面")
    private String picUrl;
    
    @ApiModelProperty("歌手名ID")
    private List<Artist> artists;
    
    @ApiModelProperty("专辑")
    private Album album;
    
    @ApiModelProperty("专辑")
    private List<MusicSources> sources;
    
    @ApiModelProperty("歌词")
    private Map<String, Lyrics> lyrics;
    
    @ApiModelProperty("歌曲时长")
    private Integer timeLength;
    
    @ApiModelProperty("发行时间")
    private LocalDateTime publishTime;
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Album implements Serializable {
        @ApiModelProperty("专辑")
        private Long albumId;
        
        @ApiModelProperty("专辑")
        private String albumName;
        
        @ApiModelProperty("歌手名ID")
        private List<Artist> artist;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Artist implements Serializable {
        @Schema(title = "歌手ID")
        private Long id;
        
        @Schema(title = "歌手名")
        private String artistName;
        
        @Schema(title = "歌手别名")
        private String aliasName;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class MusicSources extends TbResourcePojo implements Serializable {
        private String url;
        
        public MusicSources(Long id, Long musicId, Integer rate, String path, String md5, String level, String encodeType, Long size, Long userId, LocalDateTime createTime, LocalDateTime updateTime) {
            super(id, musicId, rate, path, md5, level, encodeType, size, userId, createTime, updateTime);
        }
        
        public MusicSources(TbResourcePojo pojo) {
            this(pojo.getId(), pojo.getMusicId(), pojo.getRate(), pojo.getPath(), pojo.getMd5(),
                    pojo.getLevel(), pojo.getEncodeType(), pojo.getSize(), pojo.getUserId(), pojo.getCreateTime(), pojo.getUpdateTime());
        }
    }
    
    
    @Data
    public static class Lyrics implements Serializable {
        @Schema(title = "主键")
        private Long id;
        @Schema(title = "音乐ID")
        private Long musicId;
        @Schema(title = "歌词类型")
        private String type;
        @Schema(title = "歌词")
        private String lyric;
        
        public Lyrics(Long id, Long musicId, String type, String lyric) {
            this.id = id;
            this.musicId = musicId;
            this.type = type;
            this.lyric = lyric;
        }
        
        public Lyrics(TbLyricPojo pojo) {
            this(pojo.getId(), pojo.getMusicId(), pojo.getType(), pojo.getLyric());
        }
    }
}
