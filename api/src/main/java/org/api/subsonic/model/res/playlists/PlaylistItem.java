package org.api.subsonic.model.res.playlists;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistItem {
    
    @JsonProperty("id")
    @JacksonXmlProperty(isAttribute = true)
    private String id;
    
    @JsonProperty("name")
    @JacksonXmlProperty(isAttribute = true)
    private String name;
    
    @JsonProperty("comment")
    @JacksonXmlProperty(isAttribute = true)
    private String comment;
    
    @JsonProperty("owner")
    @JacksonXmlProperty(isAttribute = true)
    private String owner;
    
    @JsonProperty("public")
    @JacksonXmlProperty(localName = "public", isAttribute = true)
    private Boolean jsonMemberPublic;
    
    @JsonProperty("songCount")
    @JacksonXmlProperty(isAttribute = true)
    private Integer songCount;
    
    @JsonProperty("duration")
    @JacksonXmlProperty(isAttribute = true)
    private Integer duration;
    
    @JsonProperty("created")
    @JacksonXmlProperty(isAttribute = true)
    private String created;
    
    @JsonProperty("coverArt")
    @JacksonXmlProperty(isAttribute = true)
    private String coverArt;
    
    @JsonProperty("changed")
    @JacksonXmlProperty(isAttribute = true)
    private String changed;
}