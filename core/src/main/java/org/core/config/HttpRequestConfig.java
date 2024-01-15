package org.core.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "http-request")
public class HttpRequestConfig {
    private Integer timeout;
    private String tempPath;
    
    public File getTempPathFile(String name) {
        return new File(this.getTempPath(), name);
    }
    
    public File getTempPathFile() {
        return new File(this.getTempPath());
    }
    
}
