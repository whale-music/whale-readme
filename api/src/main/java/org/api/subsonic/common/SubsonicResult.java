package org.api.subsonic.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.core.utils.SerializeUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "subsonic-response")
@JsonRootName("subsonic-response")
@Schema(title = "公共返回类, 所有返回类都会继承此类")
public class SubsonicResult {
    
    @JacksonXmlProperty(isAttribute = true)
    private String xmlns = "http://subsonic.org/restapi";
    
    @JacksonXmlProperty(isAttribute = true)
    private String status = "ok";
    
    @Schema(name = "subsonic api 版本")
    @JacksonXmlProperty(isAttribute = true)
    private String version = "1.16.1";
    
    @Schema(name = "服务器版本")
    @JacksonXmlProperty(isAttribute = true)
    private String serverVersion = "1.0.0";
    
    @JacksonXmlProperty(isAttribute = true)
    private Boolean openSubsonic = true;
    
    @JacksonXmlProperty(isAttribute = true)
    private String type = "whale";
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;
    
    private static final String FAILED = "failed";
    private static final String OK = "ok";
    
    public SubsonicResult success() {
        this.status = OK;
        return this;
    }
    
    public ResponseEntity<String> success(SubsonicCommonReq req) {
        this.status = OK;
        String json = req.getF();
        boolean isJson = StringUtils.equalsIgnoreCase(json, "json");
        return getResponseEntity(isJson);
    }
    
    public ResponseEntity<String> error(SubsonicCommonReq req, ErrorEnum error) {
        this.status = FAILED;
        this.error = error.error();
        String json = req.getF();
        boolean isJson = StringUtils.equalsIgnoreCase(json, "json");
        return getResponseEntity(isJson);
    }
    
    public SubsonicResult error(ErrorEnum error) {
        this.status = FAILED;
        this.error = error.error();
        return this;
    }
    
    public ResponseEntity<String> error(boolean isJson, ErrorEnum error) {
        this.status = FAILED;
        this.error = error.error();
        return getResponseEntity(isJson);
    }
    
    @NotNull
    private ResponseEntity<String> getResponseEntity(boolean isJson) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (isJson) {
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        } else {
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
        }
        return new ResponseEntity<>(SerializeUtil.serialize(this, isJson),
                httpHeaders,
                HttpStatus.OK);
    }
    
}
