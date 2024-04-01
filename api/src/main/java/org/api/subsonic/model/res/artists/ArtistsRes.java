package org.api.subsonic.model.res.artists;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ArtistsRes extends SubsonicResult {
    private Artists artists;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Artists {
        @JsonProperty("ignoredArticles")
        @JacksonXmlProperty(isAttribute = true)
        private String ignoredArticles;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(isAttribute = true)
        private List<Index> index;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Index {
        @JsonProperty("name")
        @JacksonXmlProperty(isAttribute = true)
        private String name;
        
        @JsonProperty("artist")
        @JacksonXmlProperty(isAttribute = true)
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Artist> artist;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Artist {
        @JsonProperty("id")
        @JacksonXmlProperty(isAttribute = true)
        private String id;
        
        @JsonProperty("name")
        @JacksonXmlProperty(isAttribute = true)
        private String name;
        
        @JsonProperty("coverArt")
        @JacksonXmlProperty(isAttribute = true)
        private String coverArt;
        
        @JsonProperty("albumCount")
        @JacksonXmlProperty(isAttribute = true)
        private Integer albumCount;
        
        @JsonProperty("artistImageUrl")
        @JacksonXmlProperty(isAttribute = true)
        private String artistImageUrl;
        
        @JsonProperty("userRating")
        @JacksonXmlProperty(isAttribute = true)
        private Integer userRating;
    }
}
