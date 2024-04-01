package org.api.subsonic.model.res.starred2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;
import org.api.subsonic.common.SubsonicResult;

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
        // 如果为null则不添加
        @JsonProperty("artist")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(isAttribute = true, localName = "artist")
        private List<Artist> artist;
        
        // 如果为null则不添加
        @JsonProperty("album")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(isAttribute = true, localName = "album")
        private List<Album> album;
        
        // 如果为null则不添加
        @JsonProperty("song")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(isAttribute = true, localName = "song")
        private List<Song> song;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Artist {
        @JsonProperty("id")
        @JacksonXmlProperty(isAttribute = true, localName = "id")
        private String id;
        
        @JsonProperty("name")
        @JacksonXmlProperty(isAttribute = true, localName = "name")
        private String name;
        
        @JsonProperty("albumCount")
        @JacksonXmlProperty(isAttribute = true, localName = "albumCount")
        private Integer albumCount;
        
        @JsonProperty("starred")
        @JacksonXmlProperty(isAttribute = true, localName = "starred")
        private String starred;
        
        @JsonProperty("userRating")
        @JacksonXmlProperty(isAttribute = true, localName = "userRating")
        private Integer userRating;
        
        @JsonProperty("coverArt")
        @JacksonXmlProperty(isAttribute = true, localName = "coverArt")
        private String coverArt;
        
        @JsonProperty("artistImageUrl")
        @JacksonXmlProperty(isAttribute = true, localName = "artistImageUrl")
        private String artistImageUrl;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Album {
        
        @JsonProperty("id")
        @JacksonXmlProperty(isAttribute = true, localName = "id")
        private String id;
        
        @JsonProperty("parent")
        @JacksonXmlProperty(isAttribute = true, localName = "parent")
        private String parent;
        
        @JsonProperty("isDir")
        @JacksonXmlProperty(isAttribute = true, localName = "isDir")
        private Boolean isDir;
        
        @JsonProperty("title")
        @JacksonXmlProperty(isAttribute = true, localName = "title")
        private String title;
        
        @JsonProperty("name")
        @JacksonXmlProperty(isAttribute = true, localName = "name")
        private String name;
        
        @JsonProperty("album")
        @JacksonXmlProperty(isAttribute = true, localName = "album")
        private String album;
        
        @JsonProperty("artist")
        @JacksonXmlProperty(isAttribute = true, localName = "artist")
        private String artist;
        
        @JsonProperty("year")
        @JacksonXmlProperty(isAttribute = true, localName = "year")
        private Integer year;
        
        @JsonProperty("genre")
        @JacksonXmlProperty(isAttribute = true, localName = "genre")
        private String genre;
        
        @JsonProperty("coverArt")
        @JacksonXmlProperty(isAttribute = true, localName = "coverArt")
        private String coverArt;
        
        @JsonProperty("starred")
        @JacksonXmlProperty(isAttribute = true, localName = "starred")
        private String starred;
        
        @JsonProperty("duration")
        @JacksonXmlProperty(isAttribute = true, localName = "duration")
        private Integer duration;
        
        @JsonProperty("playCount")
        @JacksonXmlProperty(isAttribute = true, localName = "playCount")
        private Integer playCount;
        
        @JsonProperty("played")
        @JacksonXmlProperty(isAttribute = true, localName = "played")
        private String played;
        
        @JsonProperty("created")
        @JacksonXmlProperty(isAttribute = true, localName = "created")
        private String created;
        
        @JsonProperty("artistId")
        @JacksonXmlProperty(isAttribute = true, localName = "artistId")
        private String artistId;
        
        @JsonProperty("userRating")
        @JacksonXmlProperty(isAttribute = true, localName = "userRating")
        private Integer userRating;
        
        @JsonProperty("songCount")
        @JacksonXmlProperty(isAttribute = true, localName = "songCount")
        private Integer songCount;
        
        @JsonProperty("isVideo")
        @JacksonXmlProperty(isAttribute = true, localName = "isVideo")
        private Boolean isVideo;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Song {
        
        @JsonProperty("id")
        @JacksonXmlProperty(isAttribute = true, localName = "id")
        private String id;
        
        @JsonProperty("parent")
        @JacksonXmlProperty(isAttribute = true, localName = "parent")
        private String parent;
        
        @JsonProperty("sortName")
        @JacksonXmlProperty(isAttribute = true, localName = "sortName")
        private String sortName;
        
        @JsonProperty("isDir")
        @JacksonXmlProperty(isAttribute = true, localName = "isDir")
        private Boolean isDir;
        
        @JsonProperty("title")
        @JacksonXmlProperty(isAttribute = true, localName = "title")
        private String title;
        
        @JsonProperty("album")
        @JacksonXmlProperty(isAttribute = true, localName = "album")
        private String album = "";
        
        @JsonProperty("artist")
        @JacksonXmlProperty(isAttribute = true, localName = "artist")
        private String artist = "";
        
        @JsonProperty("track")
        @JacksonXmlProperty(isAttribute = true, localName = "track")
        private Integer track;
        
        @JsonProperty("year")
        @JacksonXmlProperty(isAttribute = true, localName = "year")
        private Integer year;
        
        @JsonProperty("genre")
        @JacksonXmlProperty(isAttribute = true, localName = "genre")
        private String genre;
        
        @JsonProperty("coverArt")
        @JacksonXmlProperty(isAttribute = true, localName = "coverArt")
        private String coverArt;
        
        @JsonProperty("replayGain")
        @JacksonXmlProperty(isAttribute = true, localName = "replayGain")
        private ReplayGain replayGain;
        
        @JsonProperty("size")
        @JacksonXmlProperty(isAttribute = true, localName = "size")
        private Long size;
        
        @JsonProperty("contentType")
        @JacksonXmlProperty(isAttribute = true, localName = "contentType")
        private String contentType;
        
        @JsonProperty("bpm")
        @JacksonXmlProperty(isAttribute = true, localName = "bpm")
        private Integer bpm;
        
        @JsonProperty("suffix")
        @JacksonXmlProperty(isAttribute = true, localName = "suffix")
        private String suffix;
        
        @JsonProperty("starred")
        @JacksonXmlProperty(isAttribute = true, localName = "starred")
        private String starred;
        
        @JsonProperty("duration")
        @JacksonXmlProperty(isAttribute = true, localName = "duration")
        private Integer duration;
        
        @JsonProperty("bitRate")
        @JacksonXmlProperty(isAttribute = true, localName = "bitRate")
        private Integer bitRate;
        
        @JsonProperty("genres")
        @JacksonXmlProperty(isAttribute = true, localName = "genres")
        private List<Object> genres;
        
        @JsonProperty("path")
        @JacksonXmlProperty(isAttribute = true, localName = "path")
        private String path;
        
        @JsonProperty("playCount")
        @JacksonXmlProperty(isAttribute = true, localName = "playCount")
        private Integer playCount;
        
        @JsonProperty("played")
        @JacksonXmlProperty(isAttribute = true, localName = "played")
        private String played;
        
        @JsonProperty("discNumber")
        @JacksonXmlProperty(isAttribute = true, localName = "discNumber")
        private Integer discNumber;
        
        @JsonProperty("created")
        @JacksonXmlProperty(isAttribute = true, localName = "created")
        private String created;
        
        @JsonProperty("musicBrainzId")
        @JacksonXmlProperty(isAttribute = true, localName = "musicBrainzId")
        private String musicBrainzId;
        
        @JsonProperty("albumId")
        @JacksonXmlProperty(isAttribute = true, localName = "albumId")
        private String albumId = "";
        
        @JsonProperty("artistId")
        @JacksonXmlProperty(isAttribute = true, localName = "artistId")
        private String artistId = "";
        
        @JsonProperty("type")
        @JacksonXmlProperty(isAttribute = true, localName = "type")
        private String type;
        
        @JsonProperty("userRating")
        @JacksonXmlProperty(isAttribute = true, localName = "userRating")
        private Integer userRating;
        
        @JsonProperty("isVideo")
        @JacksonXmlProperty(isAttribute = true, localName = "isVideo")
        private Boolean isVideo;
        
        @JsonProperty("mediaType")
        @JacksonXmlProperty(isAttribute = true, localName = "mediaType")
        private String mediaType;
        
        @JsonProperty("comment")
        @JacksonXmlProperty(isAttribute = true, localName = "comment")
        private String comment = "";
        
        @Setter
        @Getter
        @AllArgsConstructor
        public static class ReplayGain{
            @JsonProperty("albumPeak")
            @JacksonXmlProperty(isAttribute = true, localName = "albumPeak")
            private Integer albumPeak;
            
            @JsonProperty("trackPeak")
            @JacksonXmlProperty(isAttribute = true, localName = "trackPeak")
            private Integer trackPeak;
        }
        
    }
}
