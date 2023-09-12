package org.api.subsonic.model.res.nowplaying;

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
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NowPlayingRes extends SubsonicResult {
    
    @JacksonXmlProperty(isAttribute = true, localName = "nowPlaying")
    private NowPlaying nowPlaying;
    
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NowPlaying {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(isAttribute = true, localName = "entry")
        private List<Entry> entries;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Entry {
        @JacksonXmlProperty(isAttribute = true, localName = "username")
        private String username;
        
        @JacksonXmlProperty(isAttribute = true, localName = "minutesAgo")
        private String minutesAgo;
        
        @JacksonXmlProperty(isAttribute = true, localName = "playerId")
        private String playerId;
        
        @JacksonXmlProperty(isAttribute = true, localName = "id")
        private String id;
        
        @JacksonXmlProperty(isAttribute = true, localName = "parent")
        private String parent;
        
        @JacksonXmlProperty(isAttribute = true, localName = "title")
        private String title;
        
        @JacksonXmlProperty(isAttribute = true, localName = "isDir")
        private Boolean isDir;
        
        @JacksonXmlProperty(isAttribute = true, localName = "album")
        private String album;
        
        @JacksonXmlProperty(isAttribute = true, localName = "artist")
        private String artist;
        
        @JacksonXmlProperty(isAttribute = true, localName = "track")
        private String track;
        
        @JacksonXmlProperty(isAttribute = true, localName = "year")
        private String year;
        
        @JacksonXmlProperty(isAttribute = true, localName = "genre")
        private String genre;
        
        @JacksonXmlProperty(isAttribute = true, localName = "coverArt")
        private String coverArt;
        
        @JacksonXmlProperty(isAttribute = true, localName = "size")
        private String size;
        
        @JacksonXmlProperty(isAttribute = true, localName = "contentType")
        private String contentType;
        
        @JacksonXmlProperty(isAttribute = true, localName = "suffix")
        private String suffix;
        
        @JacksonXmlProperty(isAttribute = true, localName = "bitRate")
        private String bitRate;
        
        @JacksonXmlProperty(isAttribute = true, localName = "path")
        private String path;
    }
}
