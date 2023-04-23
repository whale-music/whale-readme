package org.api.subsonic.model.res.playlist;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntryItem {
    
    @JacksonXmlProperty(isAttribute = true)
    private String parent;
    
    @JacksonXmlProperty(isAttribute = true)
    private String artist;
    
    @JacksonXmlProperty(isAttribute = true)
    private Integer year;
    
    @JacksonXmlProperty(isAttribute = true)
    private String album;
    
    @JacksonXmlProperty(isAttribute = true)
    private String created;
    
    @JacksonXmlProperty(isAttribute = true)
    private String albumId;
    
    @JacksonXmlProperty(isAttribute = true)
    private Boolean isVideo;
    
    @JacksonXmlProperty(isAttribute = true)
    private String artistId;
    
    @JacksonXmlProperty(isAttribute = true)
    private String coverArt;
    
    @JacksonXmlProperty(isAttribute = true)
    private String title;
    
    @JacksonXmlProperty(isAttribute = true)
    private String suffix;
    
    @JacksonXmlProperty(isAttribute = true)
    private String type;
    
    @JacksonXmlProperty(isAttribute = true)
    private String played;
    
    @JacksonXmlProperty(isAttribute = true)
    private Integer duration;
    
    @JacksonXmlProperty(isAttribute = true)
    private String path;
    
    @JacksonXmlProperty(isAttribute = true)
    private Integer playCount;
    
    @JacksonXmlProperty(isAttribute = true)
    private Integer size;
    
    @JacksonXmlProperty(isAttribute = true)
    private Integer bitRate;
    
    @JacksonXmlProperty(isAttribute = true)
    private String genre;
    
    @JacksonXmlProperty(isAttribute = true)
    private String id;
    
    @JacksonXmlProperty(isAttribute = true)
    private Integer track;
    
    @JacksonXmlProperty(isAttribute = true)
    private String contentType;
    
    @JacksonXmlProperty(isAttribute = true)
    private Boolean isDir;
    
    @JacksonXmlProperty(isAttribute = true)
    private Integer userRating;
    
    @JacksonXmlProperty(isAttribute = true)
    private String starred;
}