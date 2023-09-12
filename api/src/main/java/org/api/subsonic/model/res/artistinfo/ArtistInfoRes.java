package org.api.subsonic.model.res.artistinfo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.subsonic.common.SubsonicResult;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ArtistInfoRes extends SubsonicResult {
    @JacksonXmlProperty(localName = "artistInfo")
    private ArtistInfo artistInfo;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArtistInfo {
        private String biography;
        
        private String musicBrainzId;
        
        private String lastFmUrl;
        
        private String smallImageUrl;
        
        private String mediumImageUrl;
        
        private String largeImageUrl;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "similarArtist")
        private List<SimilarArtist> similarArtists;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimilarArtist {
        @JacksonXmlProperty(isAttribute = true)
        private int id;
        
        @JacksonXmlProperty(isAttribute = true)
        private String name;
    }
}
