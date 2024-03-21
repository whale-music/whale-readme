package org.api.subsonic.model.res.musicdirectory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.subsonic.common.SubsonicResult;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MusicDirectoryRes extends SubsonicResult {
    private Directory directory = new Directory();
    
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Directory {
        @JsonProperty("id")
        @JacksonXmlProperty(isAttribute = true)
        private String id;
        
        @JsonProperty("name")
        @JacksonXmlProperty(isAttribute = true)
        private String name;
        
        @Schema(name = "歌手ID")
        @JsonProperty("parent")
        @JacksonXmlProperty(isAttribute = true)
        private String parent;
        
        @Schema(name = "播放次数")
        @JsonProperty("playCount")
        @JacksonXmlProperty(isAttribute = true)
        private Integer playCount;
        
        @Schema(name = "最后一次播放时间，暂时填写歌单更新时间")
        @JsonProperty("played")
        @JacksonXmlProperty(isAttribute = true)
        private String played;
        
        @Schema(name = "用户评价等级 1-5")
        @JsonProperty("userRating")
        @JacksonXmlProperty(isAttribute = true)
        private Byte userRating = 5;
        
        @Schema(name = "封面ID")
        @JsonProperty("coverArt")
        @JacksonXmlProperty(isAttribute = true)
        private Long coverArt;
        
        @Schema(name = "歌曲数量")
        @JsonProperty("songCount")
        @JacksonXmlProperty(isAttribute = true)
        private Integer songCount;
        
        @JsonProperty("starred")
        @JacksonXmlProperty(isAttribute = true)
        private String starred;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Child> child;
    }
    
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Child {
        @JsonProperty("id")
        @JacksonXmlProperty(isAttribute = true)
        private Long id;
        
        @Schema(name = "专辑ID")
        @JsonProperty("parent")
        @JacksonXmlProperty(isAttribute = true)
        private Long parent;
        
        @JsonProperty("isDir")
        @JacksonXmlProperty(isAttribute = true)
        private Boolean isDir = false;
        
        @JsonProperty("title")
        @JacksonXmlProperty(isAttribute = true)
        private String title;
        
        @Schema(name = "专辑名")
        @JsonProperty("album")
        @JacksonXmlProperty(isAttribute = true)
        private String album;
        
        @Schema(name = "歌手名")
        @JsonProperty("artist")
        @JacksonXmlProperty(isAttribute = true)
        private String artist;
        
        @Schema(name = "歌曲在列表中的位置")
        @JsonProperty("track")
        @JacksonXmlProperty(isAttribute = true)
        private Integer track;
        
        @JsonProperty("year")
        @JacksonXmlProperty(isAttribute = true)
        private Integer year;
        
        @JsonProperty("genre")
        @JacksonXmlProperty(isAttribute = true)
        private String genre;
        
        @JsonProperty("coverArt")
        @JacksonXmlProperty(isAttribute = true)
        private Long coverArt;
        
        @JsonProperty("size")
        @JacksonXmlProperty(isAttribute = true)
        private Long size;
        
        @JsonProperty("contentType")
        @JacksonXmlProperty(isAttribute = true)
        private String contentType;
        
        @JsonProperty("suffix")
        @JacksonXmlProperty(isAttribute = true)
        private String suffix;
        
        @JsonProperty("duration")
        @JacksonXmlProperty(isAttribute = true)
        private Integer duration;
        
        @JsonProperty("bitRate")
        @JacksonXmlProperty(isAttribute = true)
        private Integer bitRate;
        
        @JsonProperty("path")
        @JacksonXmlProperty(isAttribute = true)
        private String path;
        
        @JsonProperty("playCount")
        @JacksonXmlProperty(isAttribute = true)
        private Integer playCount;
        
        @JsonProperty("created")
        @JacksonXmlProperty(isAttribute = true)
        private String created;
        
        @JsonProperty("albumId")
        @JacksonXmlProperty(isAttribute = true)
        private Long albumId;
        
        @JsonProperty("artistId")
        @JacksonXmlProperty(isAttribute = true)
        private Long artistId;
        
        @JsonProperty("type")
        @JacksonXmlProperty(isAttribute = true)
        private String type;
        
        @Schema(name = "用户评价等级 1-5")
        @JsonProperty("userRating")
        @JacksonXmlProperty(isAttribute = true)
        private Byte userRating;
        
        @JsonProperty("isVideo")
        @JacksonXmlProperty(isAttribute = true)
        private Boolean isVideo;
        
        @JsonProperty("played")
        @JacksonXmlProperty(isAttribute = true)
        private String played;
        
        @Schema(name = "音乐曲目的 BPM, 即乐曲的节奏速度")
        @JsonProperty("bpm")
        @JacksonXmlProperty(isAttribute = true)
        private Byte bpm;
        
        @JsonProperty("comment")
        @JacksonXmlProperty(isAttribute = true)
        private String comment;
        
        @Schema(name = "简称")
        @JsonProperty("sortName")
        @JacksonXmlProperty(isAttribute = true)
        private String sortName;
        
        @Schema(name = "媒体类型")
        @JsonProperty("mediaType")
        @JacksonXmlProperty(isAttribute = true)
        private String mediaType;
        
        @Schema(name = "musicBrainzId")
        @JsonProperty("musicBrainzId")
        @JacksonXmlProperty(isAttribute = true)
        private String musicBrainzId = "";
        
    }
}
