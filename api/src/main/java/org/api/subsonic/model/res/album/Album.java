package org.api.subsonic.model.res.album;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Album {
    
    @JsonProperty("song")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<SongItem> song;
    
    @JsonProperty("parent")
    @JacksonXmlProperty(isAttribute = true)
    private String parent;
    
    @JsonProperty("artist")
    @JacksonXmlProperty(isAttribute = true)
    private String artist;
    
    @JsonProperty("year")
    @JacksonXmlProperty(isAttribute = true)
    private Integer year;
    
    @JsonProperty("created")
    @JacksonXmlProperty(isAttribute = true)
    private String created;
    
    @JsonProperty("artistId")
    @JacksonXmlProperty(isAttribute = true)
    private String artistId;
    
    @JsonProperty("coverArt")
    @JacksonXmlProperty(isAttribute = true)
    private String coverArt;
    
    @JsonProperty("played")
    @JacksonXmlProperty(isAttribute = true)
    private String played;
    
    @JsonProperty("songCount")
    @JacksonXmlProperty(isAttribute = true)
    private Integer songCount;
    
    @JsonProperty("duration")
    @JacksonXmlProperty(isAttribute = true)
    private Integer duration;
    
    @JsonProperty("playCount")
    @JacksonXmlProperty(isAttribute = true)
    private Integer playCount;
    
    @JsonProperty("starred")
    @JacksonXmlProperty(isAttribute = true)
    private String starred;
    
    @JsonProperty("name")
    @JacksonXmlProperty(isAttribute = true)
    private String name;
    
    @JsonProperty("genre")
    @JacksonXmlProperty(isAttribute = true)
    private String genre;
    
    @JsonProperty("id")
    @JacksonXmlProperty(isAttribute = true)
    private String id;
}