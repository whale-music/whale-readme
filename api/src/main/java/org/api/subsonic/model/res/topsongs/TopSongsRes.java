package org.api.subsonic.model.res.topsongs;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.subsonic.common.SubsonicResult;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TopSongsRes extends SubsonicResult {
    private List<TopSongs> topSongs;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TopSongs {
        @JacksonXmlProperty(isAttribute = true)
        private String id;
        
        @JacksonXmlProperty(isAttribute = true)
        private String parent;
        
        @JacksonXmlProperty(isAttribute = true)
        private Boolean isDir;
        
        @JacksonXmlProperty(isAttribute = true)
        private String title;
        
        @JacksonXmlProperty(isAttribute = true)
        private String album;
        
        @JacksonXmlProperty(isAttribute = true)
        private String artist;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer track;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer year;
        
        @JacksonXmlProperty(isAttribute = true)
        private String coverArt;
        
        @JacksonXmlProperty(isAttribute = true)
        private Long size;
        
        @JacksonXmlProperty(isAttribute = true)
        private String contentType;
        
        @JacksonXmlProperty(isAttribute = true)
        private String suffix;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer duration;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer bitRate;
        
        @JacksonXmlProperty(isAttribute = true)
        private String path;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer playCount;
        
        @JacksonXmlProperty(isAttribute = true)
        private Date played;
        
        @JacksonXmlProperty(isAttribute = true)
        private Integer discNumber;
        
        @JacksonXmlProperty(isAttribute = true)
        private String created;
        
        @JacksonXmlProperty(isAttribute = true)
        private String albumId;
        
        @JacksonXmlProperty(isAttribute = true)
        private String artistId;
        
        @JacksonXmlProperty(isAttribute = true)
        private String type;
        
        @JacksonXmlProperty(isAttribute = true)
        private Boolean isVideo;
    }
}
