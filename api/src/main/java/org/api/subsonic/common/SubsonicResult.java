package org.api.subsonic.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JacksonXmlRootElement(localName = "subsonic-response")
@JsonRootName("subsonic-response")
@JsonIgnoreProperties({"headers", "body"})
public class SubsonicResult extends HttpEntity<SubsonicResult> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    @JacksonXmlProperty(isAttribute = true)
    private String xmlns = "http://subsonic.org/restapi";
    
    @JacksonXmlProperty(isAttribute = true)
    private String status = "ok";
    
    @JacksonXmlProperty(isAttribute = true)
    private String version = "1.16.1";
    
    @JacksonXmlProperty(isAttribute = true)
    private String type = "whale";
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;
    
    public ResponseEntity<SubsonicResult> success() {
        this.status = "ok";
        return ResponseEntity.ok(this);
    }
    
    public ResponseEntity<SubsonicResult> error(ErrorEnum error) {
        this.status = "failed";
        this.error = error.error();
        return ResponseEntity.ok(this);
    }
}
