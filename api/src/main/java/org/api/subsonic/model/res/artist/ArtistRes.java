package org.api.subsonic.model.res.artist;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.subsonic.common.SubsonicResult;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ArtistRes extends SubsonicResult {
    private Artist artist;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Artist {
        @JsonProperty("id")
        @JacksonXmlProperty(isAttribute = true)
        private String id;
        
        @JsonProperty("name")
        @JacksonXmlProperty(isAttribute = true)
        private String name;
        
        @JsonProperty("coverArt")
        @JacksonXmlProperty(isAttribute = true)
        private String coverArt;
        
        @JsonProperty("albumCount")
        @JacksonXmlProperty(isAttribute = true)
        private Integer albumCount;
        
        @JacksonXmlElementWrapper(useWrapping = false)
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
        
        @JsonProperty("isDir")
        @JacksonXmlProperty(isAttribute = true)
        private String isDir;
        
        @JsonProperty("title")
        @JacksonXmlProperty(isAttribute = true)
        private String title;
        
        @JsonProperty("name")
        @JacksonXmlProperty(isAttribute = true)
        private String name;
        
        @JsonProperty("album")
        @JacksonXmlProperty(isAttribute = true)
        private String album;
        
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
        
        @JsonProperty("year")
        @JacksonXmlProperty(isAttribute = true)
        private Integer year;
        
        @JsonProperty("played")
        @JacksonXmlProperty(isAttribute = true)
        private String played;
        
        @JsonProperty("genre")
        @JacksonXmlProperty(isAttribute = true)
        private String genre;
        
        @JsonProperty("artist")
        @JacksonXmlProperty(isAttribute = true)
        private String artist;
        
        @JsonProperty("artistId")
        @JacksonXmlProperty(isAttribute = true)
        private String artistId;
        
        @JsonProperty("userRating")
        @JacksonXmlProperty(isAttribute = true)
        private Integer userRating;
        
        @JsonProperty("isVideo")
        @JacksonXmlProperty(isAttribute = true, localName = "isVideo")
        private Boolean isVideo;
    }
}
