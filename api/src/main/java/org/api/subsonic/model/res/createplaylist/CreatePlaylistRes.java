package org.api.subsonic.model.res.createplaylist;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.subsonic.common.SubsonicResult;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreatePlaylistRes extends SubsonicResult {
    private Playlist playlist;
    
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Playlist {
        @JacksonXmlProperty(isAttribute = true)
        private String id;
        
        @JacksonXmlProperty(isAttribute = true)
        private String name;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer songCount;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer duration;
        
        @JacksonXmlProperty(isAttribute = true, localName = "public")
        private Boolean publicFlag;
        
        @JacksonXmlProperty(isAttribute = true)
        private String owner;
        
        @JacksonXmlProperty(isAttribute = true)
        private String created;
        
        @JacksonXmlProperty(isAttribute = true)
        private String changed;
        
        @JacksonXmlProperty(isAttribute = true)
        private String coverArt;
    }
    
}
