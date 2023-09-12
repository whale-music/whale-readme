package org.api.subsonic.model.res.starred2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.subsonic.common.SubsonicResult;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Starred2Res extends SubsonicResult {
    private Starred2 starred2;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Starred2 {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(isAttribute = true, localName = "artist")
        @JsonProperty("artist")
        private List<Artist> artist;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(isAttribute = true, localName = "album")
        @JsonProperty("album")
        private List<Album> album;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(isAttribute = true, localName = "song")
        @JsonProperty("song")
        private List<Song> song;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Artist {
        @JacksonXmlProperty(isAttribute = true, localName = "id")
        private String id;
        
        @JacksonXmlProperty(isAttribute = true, localName = "name")
        private String name;
        
        @JacksonXmlProperty(isAttribute = true, localName = "albumCount")
        private Integer albumCount;
        
        @JacksonXmlProperty(isAttribute = true, localName = "starred")
        private String starred;
        
        @JacksonXmlProperty(isAttribute = true, localName = "userRating")
        private Integer userRating;
        
        @JacksonXmlProperty(isAttribute = true, localName = "coverArt")
        private String coverArt;
        
        @JacksonXmlProperty(isAttribute = true, localName = "artistImageUrl")
        private String artistImageUrl;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Album {
        
        @JacksonXmlProperty(isAttribute = true, localName = "id")
        private String id;
        
        @JacksonXmlProperty(isAttribute = true, localName = "parent")
        private String parent;
        
        @JacksonXmlProperty(isAttribute = true, localName = "isDir")
        private Boolean isDir;
        
        @JacksonXmlProperty(isAttribute = true, localName = "title")
        private String title;
        
        @JacksonXmlProperty(isAttribute = true, localName = "name")
        private String name;
        
        @JacksonXmlProperty(isAttribute = true, localName = "album")
        private String album;
        
        @JacksonXmlProperty(isAttribute = true, localName = "artist")
        private String artist;
        
        @JacksonXmlProperty(isAttribute = true, localName = "year")
        private Integer year;
        
        @JacksonXmlProperty(isAttribute = true, localName = "genre")
        private String genre;
        
        @JacksonXmlProperty(isAttribute = true, localName = "coverArt")
        private String coverArt;
        
        @JacksonXmlProperty(isAttribute = true, localName = "starred")
        private String starred;
        
        @JacksonXmlProperty(isAttribute = true, localName = "duration")
        private Integer duration;
        
        @JacksonXmlProperty(isAttribute = true, localName = "playCount")
        private Integer playCount;
        
        @JacksonXmlProperty(isAttribute = true, localName = "played")
        private Date played;
        
        @JacksonXmlProperty(isAttribute = true, localName = "created")
        private Date created;
        
        @JacksonXmlProperty(isAttribute = true, localName = "artistId")
        private String artistId;
        
        @JacksonXmlProperty(isAttribute = true, localName = "userRating")
        private Integer userRating;
        
        @JacksonXmlProperty(isAttribute = true, localName = "songCount")
        private Integer songCount;
        
        @JacksonXmlProperty(isAttribute = true, localName = "isVideo")
        private Boolean isVideo;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Song {
        
        @JacksonXmlProperty(isAttribute = true, localName = "id")
        private String id;
        
        @JacksonXmlProperty(isAttribute = true, localName = "parent")
        private String parent;
        
        @JacksonXmlProperty(isAttribute = true, localName = "isDir")
        private Boolean isDir;
        
        @JacksonXmlProperty(isAttribute = true, localName = "title")
        private String title;
        
        @JacksonXmlProperty(isAttribute = true, localName = "album")
        private String album;
        
        @JacksonXmlProperty(isAttribute = true, localName = "artist")
        private String artist;
        
        @JacksonXmlProperty(isAttribute = true, localName = "track")
        private Integer track;
        
        @JacksonXmlProperty(isAttribute = true, localName = "year")
        private Integer year;
        
        @JacksonXmlProperty(isAttribute = true, localName = "genre")
        private String genre;
        
        @JacksonXmlProperty(isAttribute = true, localName = "coverArt")
        private String coverArt;
        
        @JacksonXmlProperty(isAttribute = true, localName = "size")
        private Long size;
        
        @JacksonXmlProperty(isAttribute = true, localName = "contentType")
        private String contentType;
        
        @JacksonXmlProperty(isAttribute = true, localName = "suffix")
        private String suffix;
        
        @JacksonXmlProperty(isAttribute = true, localName = "starred")
        private Date starred;
        
        @JacksonXmlProperty(isAttribute = true, localName = "duration")
        private Integer duration;
        
        @JacksonXmlProperty(isAttribute = true, localName = "bitRate")
        private Integer bitRate;
        
        @JacksonXmlProperty(isAttribute = true, localName = "path")
        private String path;
        
        @JacksonXmlProperty(isAttribute = true, localName = "playCount")
        private Integer playCount;
        
        @JacksonXmlProperty(isAttribute = true, localName = "played")
        private Date played;
        
        @JacksonXmlProperty(isAttribute = true, localName = "discNumber")
        private Integer discNumber;
        
        @JacksonXmlProperty(isAttribute = true, localName = "created")
        private Date created;
        
        @JacksonXmlProperty(isAttribute = true, localName = "albumId")
        private String albumId;
        
        @JacksonXmlProperty(isAttribute = true, localName = "artistId")
        private String artistId;
        
        @JacksonXmlProperty(isAttribute = true, localName = "type")
        private String type;
        
        @JacksonXmlProperty(isAttribute = true, localName = "userRating")
        private Integer userRating;
        
        @JacksonXmlProperty(isAttribute = true, localName = "isVideo")
        private Boolean isVideo;
    }
}
