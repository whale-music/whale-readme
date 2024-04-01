package org.api.subsonic.model.res.search;

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
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SearchRes extends SubsonicResult {
    @JsonProperty("searchResult")
    private SearchResult searchResult;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchResult {
        @JacksonXmlProperty(isAttribute = true)
        private Integer offset;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer totalHits;
        
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "match")
        @JsonProperty("match")
        private List<Match> matches;
        
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Match {
        @JacksonXmlProperty(isAttribute = true)
        private String id;
        
        @JacksonXmlProperty(isAttribute = true)
        private String parent;
        
        @JacksonXmlProperty(isAttribute = true)
        private String title;
        
        @JacksonXmlProperty(isAttribute = true)
        private Boolean isDir;
        
        @JacksonXmlProperty(isAttribute = true)
        private String album;
        
        @JacksonXmlProperty(isAttribute = true)
        private String albumId;
        
        @JacksonXmlProperty(isAttribute = true)
        private String artist;
        
        @JacksonXmlProperty(isAttribute = true)
        private String artistId;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer track;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer year;
        
        @JacksonXmlProperty(isAttribute = true)
        private String genre;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer coverArt;
        
        @JacksonXmlProperty(isAttribute = true)
        private long size;
        
        @JacksonXmlProperty(isAttribute = true)
        private String contentType;
        
        @JacksonXmlProperty(isAttribute = true)
        private String suffix;
        
        @JacksonXmlProperty(isAttribute = true)
        private String path;
        
        @JacksonXmlProperty(isAttribute = true)
        private String transcodedContentType;
        
        @JacksonXmlProperty(isAttribute = true)
        private String transcodedSuffix;
        
    }
}
