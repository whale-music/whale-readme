package org.api.subsonic.model.res.videos;

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
public class VideosRes extends SubsonicResult {
    private Videos videos;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Videos {
        
        private List<Video> video;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Video {
        @JacksonXmlProperty(isAttribute = true)
        private String id;
        
        @JacksonXmlProperty(isAttribute = true)
        private String parent;
        
        @JacksonXmlProperty(isAttribute = true)
        private String title;
        
        @JacksonXmlProperty(isAttribute = true)
        private String album;
        
        @JacksonXmlProperty(isAttribute = true)
        private String isDir;
        
        @JacksonXmlProperty(isAttribute = true)
        private String coverArt;
        
        @JacksonXmlProperty(isAttribute = true)
        private String created;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer duration;
        
        @JacksonXmlProperty(isAttribute = true)
        private String bitRate;
        
        @JacksonXmlProperty(isAttribute = true)
        private String size;
        
        @JacksonXmlProperty(isAttribute = true)
        private String suffix;
        
        @JacksonXmlProperty(isAttribute = true)
        private String contentType;
        
        @JacksonXmlProperty(isAttribute = true, localName = "isVideo")
        private Boolean isVideo;
        
        @JacksonXmlProperty(isAttribute = true)
        private String path;
        
        @JacksonXmlProperty(isAttribute = true)
        private String transcodedSuffix;
        
        @JacksonXmlProperty(isAttribute = true)
        private String transcodedContentType;
        
    }
}
