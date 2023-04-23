package org.api.subsonic.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Error {
    
    @JacksonXmlProperty(isAttribute = true)
    private Integer code;
    
    @JacksonXmlProperty(isAttribute = true)
    private String message;
}
