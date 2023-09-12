package org.api.subsonic.model.res.videoinfo;

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
public class VideoInfoRes extends SubsonicResult {
    private VideoInfo videoInfo;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VideoInfo {
        @JacksonXmlProperty(isAttribute = true)
        private int id;
        
        @JacksonXmlProperty(localName = "captions")
        private Captions captions;
        
        @JacksonXmlProperty(localName = "audioTrack")
        private List<AudioTrack> audioTrack;
        
        @JacksonXmlProperty(localName = "conversion")
        private Conversion conversion;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Captions {
        @JacksonXmlProperty(isAttribute = true)
        private int id;
        
        @JacksonXmlProperty(isAttribute = true)
        private String name;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AudioTrack {
        @JacksonXmlProperty(isAttribute = true)
        private int id;
        
        @JacksonXmlProperty(isAttribute = true)
        private String name;
        
        @JacksonXmlProperty(isAttribute = true)
        private String languageCode;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Conversion {
        @JacksonXmlProperty(isAttribute = true)
        private int id;
        
        @JacksonXmlProperty(isAttribute = true)
        private int bitRate;
    }
}
