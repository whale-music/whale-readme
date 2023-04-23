package org.api.subsonic.model.res.album;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JacksonXmlProperty(isAttribute = true)
    private List<SongItem> song;
    
    @JsonProperty("artist")
    @JacksonXmlProperty(isAttribute = true)
    private String artist;
    
    @JsonProperty("year")
    @JacksonXmlProperty(isAttribute = true)
    private int year;
    
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
    private int songCount;
    
    @JsonProperty("duration")
    @JacksonXmlProperty(isAttribute = true)
    private int duration;
    
    @JsonProperty("playCount")
    @JacksonXmlProperty(isAttribute = true)
    private int playCount;
    
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