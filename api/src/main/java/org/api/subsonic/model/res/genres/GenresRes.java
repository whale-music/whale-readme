package org.api.subsonic.model.res.genres;

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
public class GenresRes extends SubsonicResult {
    private List<Genres> genres;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Genres {
        @JsonProperty("songCount")
        @JacksonXmlProperty(isAttribute = true)
        private String songCount;
        
        @JsonProperty("albumCount")
        @JacksonXmlProperty(isAttribute = true)
        private String albumCount;
        
        private String genre;
    }
}
