package org.api.subsonic.model.res.albuminfo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.subsonic.common.SubsonicResult;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AlbumInfoRes extends SubsonicResult {
    private AlbumInfo albumInfo;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AlbumInfo {
        @JacksonXmlProperty(localName = "notes")
        private String notes;
        
        @JacksonXmlProperty(localName = "musicBrainzId")
        private String musicBrainzId;
        
        @JacksonXmlProperty(localName = "lastFmUrl")
        private String lastFmUrl;
        
        @JacksonXmlProperty(localName = "smallImageUrl")
        private String smallImageUrl;
        
        @JacksonXmlProperty(localName = "mediumImageUrl")
        private String mediumImageUrl;
        
        @JacksonXmlProperty(localName = "largeImageUrl")
        private String largeImageUrl;
        
    }
}
