package org.api.subsonic.model.res.playlist;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Playlist {
    @JacksonXmlProperty(isAttribute = true)
	@JsonAlias("public")
    private Boolean publicStr;
    
    @JacksonXmlProperty(isAttribute = true)
    private Integer duration;
    
    @JacksonXmlProperty(isAttribute = true)
    private String owner;
    
    @JacksonXmlProperty(isAttribute = true)
    private String created;
    
    @JacksonXmlProperty(isAttribute = true)
    private String name;
    
    @JacksonXmlProperty(isAttribute = true)
    private String id;
    
    @JacksonXmlProperty(isAttribute = true)
    private String coverArt;
    
    @JacksonXmlProperty(isAttribute = true)
    private Integer songCount;
    
    @JacksonXmlProperty(isAttribute = true)
    private String changed;
    
    private List<EntryItem> entry;
}