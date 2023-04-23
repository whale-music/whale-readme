package org.api.subsonic.model.res.license;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.subsonic.common.SubsonicResult;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LicenseRes extends SubsonicResult {
    
    private License license;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonRootName("license")
    public static class License {
        
        @JacksonXmlProperty(isAttribute = true)
        private Boolean valid;
    }
}
