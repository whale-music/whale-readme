package org.api.subsonic.model.res.scanstatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.subsonic.common.SubsonicResult;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ScanStatusRes extends SubsonicResult {
    
    @JsonProperty("scanStatus")
    @JacksonXmlElementWrapper(useWrapping = false)
    private ScanStatus scanStatus;
    
    @Data
    @AllArgsConstructor
    public static class ScanStatus {
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
