package org.api.subsonic.model.res.scanstatus;

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
    private ScanStatus scanStatus;
    
    @Data
    @AllArgsConstructor
    public static class ScanStatus {
        @JacksonXmlProperty(isAttribute = true)
        private Boolean scanning;
        
        @JacksonXmlProperty(isAttribute = true)
        private Long count;
    }
}
