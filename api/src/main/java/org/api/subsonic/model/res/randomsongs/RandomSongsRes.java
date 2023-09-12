package org.api.subsonic.model.res.randomsongs;

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
public class RandomSongsRes extends SubsonicResult {
    private RandomSongs randomSongs;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RandomSongs {
        
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Song> song;
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
        private String track;
        
        @JacksonXmlProperty(isAttribute = true)
        private String year;
        
        @JacksonXmlProperty(isAttribute = true)
        private String genre;
        
        @JacksonXmlProperty(isAttribute = true)
        private String coverArt;
        
        @JacksonXmlProperty(isAttribute = true)
        private String size;
        
        @JacksonXmlProperty(isAttribute = true)
        private String contentType;
        
        @JacksonXmlProperty(isAttribute = true)
        private String suffix;
        
        @JacksonXmlProperty(isAttribute = true)
        private String duration;
        
        @JacksonXmlProperty(isAttribute = true)
        private String bitRate;
        
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
        
        @JacksonXmlProperty(isAttribute = true)
        private String transcodedContentType;
        
        @JacksonXmlProperty(isAttribute = true)
        private String transcodedSuffix;
    }
}
