package org.api.subsonic.model.res.similarsongs;

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
public class SimilarSongsRes extends SubsonicResult {
    @JacksonXmlProperty(localName = "similarSongs")
    private SimilarSongs similarSongs;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimilarSongs {
        @JacksonXmlProperty(localName = "song")
        private List<Song> songs;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Song {
        @JacksonXmlProperty(isAttribute = true)
        private int id;
        
        @JacksonXmlProperty(isAttribute = true)
        private String parent;
        
        @JacksonXmlProperty(isAttribute = true)
        private boolean isDir;
        
        @JacksonXmlProperty(isAttribute = true)
        private String title;
        
        @JacksonXmlProperty(isAttribute = true)
        private String album;
        
        @JacksonXmlProperty(isAttribute = true)
        private String artist;
        
        @JacksonXmlProperty(isAttribute = true)
        private int track;
        
        @JacksonXmlProperty(isAttribute = true)
        private int year;
        
        @JacksonXmlProperty(isAttribute = true)
        private String coverArt;
        
        @JacksonXmlProperty(isAttribute = true)
        private long size;
        
        @JacksonXmlProperty(isAttribute = true)
        private String contentType;
        
        @JacksonXmlProperty(isAttribute = true)
        private String suffix;
        
        @JacksonXmlProperty(isAttribute = true)
        private int duration;
        
        @JacksonXmlProperty(isAttribute = true)
        private int bitRate;
        
        @JacksonXmlProperty(isAttribute = true)
        private String path;
        
        @JacksonXmlProperty(isAttribute = true)
        private int playCount;
        
        @JacksonXmlProperty(isAttribute = true)
        private Date played;
        
        @JacksonXmlProperty(isAttribute = true)
        private int discNumber;
        
        @JacksonXmlProperty(isAttribute = true)
        private String created;
        
        @JacksonXmlProperty(isAttribute = true)
        private String albumId;
        
        @JacksonXmlProperty(isAttribute = true)
        private String artistId;
        
        @JacksonXmlProperty(isAttribute = true)
        private String type;
        
        @JacksonXmlProperty(isAttribute = true)
        private boolean isVideo;
    }
}
