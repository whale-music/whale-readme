package org.api.subsonic.model.res.musicfolders;

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
public class MusicFoldersRes extends SubsonicResult {
    private MusicFolders musicFolders;
    
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MusicFolders {
        private List<MusicFolder> musicFolder;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MusicFolder {
        @JsonProperty("id")
        @JacksonXmlProperty(isAttribute = true)
        private Long id;
        
        @JsonProperty("name")
        @JacksonXmlProperty(isAttribute = true)
        private String name;
    }
}
