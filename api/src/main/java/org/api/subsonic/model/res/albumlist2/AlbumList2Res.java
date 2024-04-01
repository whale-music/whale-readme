package org.api.subsonic.model.res.albumlist2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.subsonic.common.SubsonicResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(title = "专辑列表")
public class AlbumList2Res extends SubsonicResult {
    
    private AlbumList2 albumList2;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AlbumList2 {
        
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("album")
        private List<AlbumItem> album;
        
        
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class AlbumItem {
            
            @JsonProperty("id")
            @JacksonXmlProperty(isAttribute = true)
            private String id;
            
            @JsonProperty("parent")
            @JacksonXmlProperty(isAttribute = true)
            private String parent;
            
            @JsonProperty("album")
            @JacksonXmlProperty(isAttribute = true)
            private String album;
            
            @JsonProperty("title")
            @JacksonXmlProperty(isAttribute = true)
            private String title;
            
            @JsonProperty("name")
            @JacksonXmlProperty(isAttribute = true)
            private String name;
            
            @JsonProperty("isDir")
            @JacksonXmlProperty(isAttribute = true, localName = "isDir")
            private Boolean isDir;
            
            @JsonProperty("coverArt")
            @JacksonXmlProperty(isAttribute = true)
            private String coverArt;
            
            @JsonProperty("songCount")
            @JacksonXmlProperty(isAttribute = true)
            private Integer songCount;
            
            @JsonProperty("created")
            @JacksonXmlProperty(isAttribute = true)
            private String created;
            
            @JsonProperty("duration")
            @JacksonXmlProperty(isAttribute = true)
            private Integer duration;
            
            @JsonProperty("playCount")
            @JacksonXmlProperty(isAttribute = true)
            private Integer playCount;
            
            @JsonProperty("artistId")
            @JacksonXmlProperty(isAttribute = true)
            private String artistId;
            
            @JsonProperty("artist")
            @JacksonXmlProperty(isAttribute = true)
            private String artist;
            
            @JsonProperty("year")
            @JacksonXmlProperty(isAttribute = true)
            private Integer year;
            
            @JsonProperty("genres")
            @JacksonXmlProperty(isAttribute = true)
            private List<String> genres;
            
            @JsonProperty("sortName")
            @JacksonXmlProperty(isAttribute = true)
            private String sortName;
            
            @JsonProperty("musicBrainzId")
            @JacksonXmlProperty(isAttribute = true)
            private String musicBrainzId;
            
            @JsonProperty("isVideo")
            @JacksonXmlProperty(isAttribute = true, localName = "isVideo")
            private Boolean isVideo;
            
            @JsonProperty("mediaType")
            @JacksonXmlProperty(isAttribute = true)
            private String mediaType;
            
            @JsonProperty("played")
            @JacksonXmlProperty(isAttribute = true)
            private String played;
            
            @JsonProperty("userRating")
            @JacksonXmlProperty(isAttribute = true)
            private Integer userRating;
            
            @Schema(name = "保留字段, 文档没有对应字段信息")
            @JsonProperty("replayGain")
            @JacksonXmlProperty(isAttribute = true)
            private Map<String, String> replayGain = new HashMap<>();
            
            @JsonProperty("comment")
            @JacksonXmlProperty(isAttribute = true)
            private String comment;
            
            @JsonProperty("genre")
            @JacksonXmlProperty(isAttribute = true)
            private String genre;
            
            @JsonProperty("bpm")
            @JacksonXmlProperty(isAttribute = true)
            private Integer bpm;
            
            @JsonProperty("starred")
            @JacksonXmlProperty(isAttribute = true)
            private String starred;
        }
    }
}
