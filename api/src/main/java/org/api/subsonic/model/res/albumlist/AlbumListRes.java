package org.api.subsonic.model.res.albumlist;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
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
public class AlbumListRes extends SubsonicResult {
    private AlbumList albumList;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AlbumList {
        private List<Album> album;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Album {
        @JsonProperty("id")
        @JacksonXmlProperty(isAttribute = true)
        private String id;
        
        @JsonProperty("parent")
        @JacksonXmlProperty(isAttribute = true)
        private String parent;
        
        @JsonProperty("title")
        @JacksonXmlProperty(isAttribute = true)
        private String title;
        
        @JsonProperty("artist")
        @JacksonXmlProperty(isAttribute = true)
        private String artist;
        
        @JsonProperty("isDir")
        @JacksonXmlProperty(isAttribute = true)
        private Boolean isDir;
        
        @JsonProperty("coverArt")
        @JacksonXmlProperty(isAttribute = true)
        private String coverArt;
        
        @JsonProperty("userRating")
        @JacksonXmlProperty(isAttribute = true)
        private Integer userRating;
        
        @JsonProperty("year")
        @JacksonXmlProperty(isAttribute = true)
        private Integer year;
        
        @JsonProperty("album")
        @JacksonXmlProperty(isAttribute = true)
        private String album;
        
        @JsonProperty("created")
        @JacksonXmlProperty(isAttribute = true)
        private String created;
        
        @JsonProperty("isVideo")
        @JacksonXmlProperty(isAttribute = true)
        private Boolean isVideo;
        
        @JsonProperty("artistId")
        @JacksonXmlProperty(isAttribute = true)
        private String artistId;
        
        @JsonProperty("played")
        @JacksonXmlProperty(isAttribute = true)
        private String played;
        
        @JsonProperty("songCount")
        @JacksonXmlProperty(isAttribute = true)
        private Integer songCount;
        
        @JsonProperty("duration")
        @JacksonXmlProperty(isAttribute = true)
        private Integer duration;
        
        @JsonProperty("playCount")
        @JacksonXmlProperty(isAttribute = true)
        private Integer playCount;
        
        @JsonProperty("name")
        @JacksonXmlProperty(isAttribute = true)
        private String name;
        
        @JsonProperty("genre")
        @JacksonXmlProperty(isAttribute = true)
        private String genre;
        
        @JsonProperty("starred")
        @JacksonXmlProperty(isAttribute = true)
        private String starred;
    }
}
