package org.api.subsonic.model.res.starred2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Starred2 {
    
    @JsonProperty("artist")
    private List<Artist> artist;
    
    @JsonProperty("album")
    private List<Album> album;
    
    @JsonProperty("song")
    private List<Song> song;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Artist {
        private String id;
        private String name;
        private int albumCount;
        private Date starred;
        private int userRating;
        private String coverArt;
        private String artistImageUrl;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Album {
        private String id;
        private String parent;
        private boolean isDir;
        private String title;
        private String name;
        private String album;
        private String artist;
        private int year;
        private String genre;
        private String coverArt;
        private Date starred;
        private int duration;
        private int playCount;
        private Date played;
        private Date created;
        private String artistId;
        private int userRating;
        private int songCount;
        private boolean isVideo;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Song {
        private String id;
        private String parent;
        private boolean isDir;
        private String title;
        private String album;
        private String artist;
        private int track;
        private int year;
        private String coverArt;
        private long size;
        private String contentType;
        private String suffix;
        private Date starred;
        private int duration;
        private int bitRate;
        private String path;
        private int playCount;
        private Date played;
        private int discNumber;
        private Date created;
        private String albumId;
        private String artistId;
        private String type;
        private int userRating;
        private boolean isVideo;
    }
}
