package org.api.subsonic.model.res.randomsongs;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;
import org.api.subsonic.common.SubsonicResult;

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
        private Integer track;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer year;
        
        @JacksonXmlProperty(isAttribute = true)
        private String genre;
        
        @JacksonXmlProperty(isAttribute = true)
        private Genres genres;
        
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
        private String played;
        
        @JacksonXmlProperty(isAttribute = true)
        private String created;
        
        @JacksonXmlProperty(isAttribute = true)
        private String albumId;
        
        @JacksonXmlProperty(isAttribute = true)
        private String artistId;
        
        @JacksonXmlProperty(isAttribute = true)
        private String type;
        
        @JacksonXmlProperty(isAttribute = true, localName = "isVideo")
        private Boolean isVideo;
        
        @JacksonXmlProperty(isAttribute = true)
        private String transcodedContentType;
        
        @JacksonXmlProperty(isAttribute = true)
        private String transcodedSuffix;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer userRating;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer bpm;
        
        @JacksonXmlProperty(isAttribute = true)
        private String comment;
        
        @JacksonXmlProperty(isAttribute = true)
        private String sortName;
        
        @JacksonXmlProperty(isAttribute = true)
        private String mediaType;
        
        @JacksonXmlProperty(isAttribute = true)
        private String starred;
        
        @JacksonXmlProperty(isAttribute = true)
        private String musicBrainzId;
        
        @JacksonXmlProperty(isAttribute = true)
        private ReplayGain replayGain;
        
        @Setter
        @Getter
        @AllArgsConstructor
        public static class Genres {
            private String name;
        }
        
        @Setter
        @Getter
        @AllArgsConstructor
        public static class ReplayGain {
            private Integer trackPeak;
            private Integer albumPeak;
        }
    }
}
