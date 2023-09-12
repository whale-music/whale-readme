package org.api.subsonic.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Error implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    @JacksonXmlProperty(isAttribute = true)
    private Integer code;
    
    @JacksonXmlProperty(isAttribute = true)
    private String message;
}
