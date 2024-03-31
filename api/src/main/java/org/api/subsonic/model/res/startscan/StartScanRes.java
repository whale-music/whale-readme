package org.api.subsonic.model.res.startscan;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;
import org.api.subsonic.common.SubsonicResult;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class StartScanRes extends SubsonicResult {
    
    @JsonProperty("scanStatus")
    @JacksonXmlElementWrapper(useWrapping = false)
    private ScanStatus scanStatus;
    
    @Getter
    @Setter
    @AllArgsConstructor
    public static class ScanStatus{
        
        @JsonProperty("lastScan")
        @JacksonXmlProperty(isAttribute = true)
        private String lastScan;
        
        @JsonProperty("scanning")
        @JacksonXmlProperty(isAttribute = true)
        private Boolean scanning;
        
        @JsonProperty("count")
        @JacksonXmlProperty(isAttribute = true)
        private Long count;
        
        @JsonProperty("folderCount")
        @JacksonXmlProperty(isAttribute = true)
        private Integer folderCount;
    }
    
}
