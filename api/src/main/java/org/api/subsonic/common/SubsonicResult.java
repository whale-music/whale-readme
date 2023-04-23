package org.api.subsonic.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "subsonic-response")
@JsonRootName("subsonic-response")
public class SubsonicResult {
    
    // @JacksonXmlProperty(isAttribute = true)
    // private String xmlns = "http://subsonic.org/restapi";
    
    @JacksonXmlProperty(isAttribute = true)
    private String status = "ok";
    
    @JacksonXmlProperty(isAttribute = true)
    private String version = "1.16.1";
    
    @JacksonXmlProperty(isAttribute = true)
    private String type = "whale";
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;
    
    public SubsonicResult success() {
        this.status = "ok";
        return this;
    }
    
    public SubsonicResult error(ErrorEnum error) {
        this.status = "failed";
        this.error = error.error();
        return this;
    }
}
