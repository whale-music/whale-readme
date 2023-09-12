package org.api.subsonic.model.res.search2;

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
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Search2Res extends SubsonicResult {
    private SearchResult2 searchResult2;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchResult2 {
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Artist> artist;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Album> album;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Song> song;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Artist {
        @JacksonXmlProperty(isAttribute = true)
        private String id;
        
        @JacksonXmlProperty(isAttribute = true)
        private String name;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer albumCount;
        
        @JacksonXmlProperty(isAttribute = true)
        private String starred;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer userRating;
        
        @JacksonXmlProperty(isAttribute = true)
        private String coverArt;
        
        @JacksonXmlProperty(isAttribute = true)
        private String artistImageUrl;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Album {
        
        @JacksonXmlProperty(isAttribute = true)
        private String id;
        
        @JacksonXmlProperty(isAttribute = true)
        private String parent;
        
        @JacksonXmlProperty(isAttribute = true)
        private Boolean isDir;
        
        @JacksonXmlProperty(isAttribute = true)
        private String title;
        
        @JacksonXmlProperty(isAttribute = true)
        private String name;
        
        @JacksonXmlProperty(isAttribute = true)
        private String album;
        
        @JacksonXmlProperty(isAttribute = true)
        private String artist;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer year;
        
        @JacksonXmlProperty(isAttribute = true)
        private String genre;
        
        @JacksonXmlProperty(isAttribute = true)
        private String coverArt;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer duration;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer playCount;
        
        @JacksonXmlProperty(isAttribute = true)
        private Date played;
        
        @JacksonXmlProperty(isAttribute = true)
        private Date created;
        
        @JacksonXmlProperty(isAttribute = true)
        private String artistId;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer songCount;
        
        @JacksonXmlProperty(isAttribute = true)
        private Boolean isVideo;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Song {
        
        @JacksonXmlProperty(isAttribute = true)
        private String id;
        
        @JacksonXmlProperty(isAttribute = true)
        private String parent;
        
        @JacksonXmlProperty(isAttribute = true)
        private Boolean isDir;
        
        @JacksonXmlProperty(isAttribute = true)
        private String title;
        
        @JacksonXmlProperty(isAttribute = true)
        private String album;
        
        @JacksonXmlProperty(isAttribute = true)
        private String artist;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer track;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer year;
        
        @JacksonXmlProperty(isAttribute = true)
        private String genre;
        
        @JacksonXmlProperty(isAttribute = true)
        private String coverArt;
        
        @JacksonXmlProperty(isAttribute = true)
        private Long size;
        
        @JacksonXmlProperty(isAttribute = true)
        private String contentType;
        
        @JacksonXmlProperty(isAttribute = true)
        private String suffix;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer duration;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer bitRate;
        
        @JacksonXmlProperty(isAttribute = true)
        private String path;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer playCount;
        
        @JacksonXmlProperty(isAttribute = true)
        private Date played;
        
        @JacksonXmlProperty(isAttribute = true)
        private Date created;
        
        @JacksonXmlProperty(isAttribute = true)
        private String albumId;
        
        @JacksonXmlProperty(isAttribute = true)
        private String artistId;
        
        @JacksonXmlProperty(isAttribute = true)
        private String type;
        
        @JacksonXmlProperty(isAttribute = true)
        private Boolean isVideo;
    }
}
