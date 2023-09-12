package org.api.subsonic.model.res.musicdirectory;

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
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MusicDirectoryRes extends SubsonicResult {
    private Directory directory;
    
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Directory {
        @JsonProperty("id")
        @JacksonXmlProperty(isAttribute = true)
        private String id;
        
        @JsonProperty("parent")
        @JacksonXmlProperty(isAttribute = true)
        private String parent;
        
        @JsonProperty("name")
        @JacksonXmlProperty(isAttribute = true)
        private String name;
        
        @JsonProperty("starred")
        @JacksonXmlProperty(isAttribute = true)
        private String starred;
        
        private List<Child> child;
    }
    
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Child {
        @JsonProperty("id")
        @JacksonXmlProperty(isAttribute = true)
        private String id;
        
        @JsonProperty("parent")
        @JacksonXmlProperty(isAttribute = true)
        private String parent;
        
        @JsonProperty("title")
        @JacksonXmlProperty(isAttribute = true)
        private String title;
        
        @JsonProperty("isDir")
        @JacksonXmlProperty(isAttribute = true)
        private String isDir;
        
        @JsonProperty("album")
        @JacksonXmlProperty(isAttribute = true)
        private String album;
        
        @JsonProperty("artist")
        @JacksonXmlProperty(isAttribute = true)
        private String artist;
        
        @JsonProperty("track")
        @JacksonXmlProperty(isAttribute = true)
        private String track;
        
        @JsonProperty("year")
        @JacksonXmlProperty(isAttribute = true)
        private String year;
        
        @JsonProperty("genre")
        @JacksonXmlProperty(isAttribute = true)
        private String genre;
        
        @JsonProperty("coverArt")
        @JacksonXmlProperty(isAttribute = true)
        private String coverArt;
        
        @JsonProperty("size")
        @JacksonXmlProperty(isAttribute = true)
        private String size;
        
        @JsonProperty("contentType")
        @JacksonXmlProperty(isAttribute = true)
        private String contentType;
        
        @JsonProperty("suffix")
        @JacksonXmlProperty(isAttribute = true)
        private String suffix;
        
        @JsonProperty("duration")
        @JacksonXmlProperty(isAttribute = true)
        private String duration;
        
        @JsonProperty("bitRate")
        @JacksonXmlProperty(isAttribute = true)
        private String bitRate;
        
        @JsonProperty("path")
        @JacksonXmlProperty(isAttribute = true)
        private String path;
        
    }
}
