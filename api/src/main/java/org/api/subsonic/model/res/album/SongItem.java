package org.api.subsonic.model.res.album;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongItem {
    
    @JsonProperty("parent")
    @JacksonXmlProperty(isAttribute = true)
    private String parent;
    
    @JsonProperty("artist")
    @JacksonXmlProperty(isAttribute = true)
    private String artist;
    
    @JsonProperty("year")
    @JacksonXmlProperty(isAttribute = true)
    private int year;
    
    @JsonProperty("album")
    @JacksonXmlProperty(isAttribute = true)
    private String album;
    
    @JsonProperty("created")
    @JacksonXmlProperty(isAttribute = true)
    private String created;
    
    @JsonProperty("albumId")
    @JacksonXmlProperty(isAttribute = true)
    private String albumId;
    
    @JsonProperty("isVideo")
    @JacksonXmlProperty(isAttribute = true)
    private boolean isVideo;
    
    @JsonProperty("artistId")
    @JacksonXmlProperty(isAttribute = true)
    private String artistId;
    
    @JsonProperty("coverArt")
    @JacksonXmlProperty(isAttribute = true)
    private String coverArt;
    
    @JsonProperty("title")
    @JacksonXmlProperty(isAttribute = true)
    private String title;
    
    @JsonProperty("suffix")
    @JacksonXmlProperty(isAttribute = true)
    private String suffix;
    
    @JsonProperty("type")
    @JacksonXmlProperty(isAttribute = true)
    private String type;
    
    @JsonProperty("played")
    @JacksonXmlProperty(isAttribute = true)
    private String played;
    
    @JsonProperty("duration")
    @JacksonXmlProperty(isAttribute = true)
    private int duration;
    
    @JsonProperty("path")
    @JacksonXmlProperty(isAttribute = true)
    private String path;
    
    @JsonProperty("playCount")
    @JacksonXmlProperty(isAttribute = true)
    private int playCount;
    
    @JsonProperty("size")
    @JacksonXmlProperty(isAttribute = true)
    private int size;
    
    @JsonProperty("bitRate")
    @JacksonXmlProperty(isAttribute = true)
    private int bitRate;
    
    @JsonProperty("genre")
    @JacksonXmlProperty(isAttribute = true)
    private String genre;
    
    @JsonProperty("id")
    @JacksonXmlProperty(isAttribute = true)
    private String id;
    
    @JsonProperty("track")
    @JacksonXmlProperty(isAttribute = true)
    private int track;
    
    @JsonProperty("contentType")
    @JacksonXmlProperty(isAttribute = true)
    private String contentType;
    
    @JsonProperty("isDir")
    @JacksonXmlProperty(isAttribute = true)
    private boolean isDir;
    
    @JsonProperty("starred")
    @JacksonXmlProperty(isAttribute = true)
    private String starred;
    
    @JsonProperty("userRating")
    @JacksonXmlProperty(isAttribute = true)
    private int userRating;
}