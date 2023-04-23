package org.api.subsonic.model.res.albumlist2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumItem {
    
    @JsonProperty("parent")
    @JacksonXmlProperty(isAttribute = true)
    private String parent;
    
    @JsonProperty("artist")
    @JacksonXmlProperty(isAttribute = true)
    private String artist;
    
    @JsonProperty("year")
    @JacksonXmlProperty(isAttribute = true)
    private Integer year;
    
    @JsonProperty("album")
    @JacksonXmlProperty(isAttribute = true)
    private String album;
    
    @JsonProperty("created")
    @JacksonXmlProperty(isAttribute = true)
    private String created;
    
    @JsonProperty("isVideo")
    @JacksonXmlProperty(isAttribute = true)
    private Boolean isVideo;
    
    @JsonProperty("artistId")
    @JacksonXmlProperty(isAttribute = true)
    private String artistId;
    
    @JsonProperty("coverArt")
    @JacksonXmlProperty(isAttribute = true)
    private String coverArt;
    
    @JsonProperty("title")
    @JacksonXmlProperty(isAttribute = true)
    private String title;
    
    @JsonProperty("played")
    @JacksonXmlProperty(isAttribute = true)
    private String played;
    
    @JsonProperty("userRating")
    @JacksonXmlProperty(isAttribute = true)
    private Integer userRating;
    
    @JsonProperty("songCount")
    @JacksonXmlProperty(isAttribute = true)
    private Integer songCount;
    
    @JsonProperty("duration")
    @JacksonXmlProperty(isAttribute = true)
    private Integer duration;
    
    @JsonProperty("playCount")
    @JacksonXmlProperty(isAttribute = true)
    private Integer playCount;
    
    @JsonProperty("name")
    @JacksonXmlProperty(isAttribute = true)
    private String name;
    
    @JsonProperty("genre")
    @JacksonXmlProperty(isAttribute = true)
    private String genre;
    
    @JsonProperty("id")
    @JacksonXmlProperty(isAttribute = true)
    private String id;
    
    @JsonProperty("isDir")
    @JacksonXmlProperty(isAttribute = true)
    private Boolean isDir;
    
    @JsonProperty("starred")
    @JacksonXmlProperty(isAttribute = true)
    private String starred;
}