package org.api.subsonic.model.res.indexes;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class IndexesRes extends SubsonicResult {
    
    private Indexes indexes;
    
    @JsonProperty("lastModified")
    private Long lastModified;
    
    @JsonProperty("ignoredArticles")
    private String ignoredArticles;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Indexes {
        private List<Index> index;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Index {
        @JsonProperty("name")
        private String name;
        
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
        
        @JsonProperty("albumCount")
        @JacksonXmlProperty(isAttribute = true)
        private Integer albumCount;
        
        @JsonProperty("userRating")
        @JacksonXmlProperty(isAttribute = true)
        private Integer userRating;
        
        @JsonProperty("coverArt")
        @JacksonXmlProperty(isAttribute = true)
        private String coverArt;
        
        @JsonProperty("artistImageUrl")
        @JacksonXmlProperty(isAttribute = true)
        private String artistImageUrl;
    }
}
