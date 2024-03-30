package org.core.common.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
public class DebugConfig {
    private static Boolean DEBUG;
    
    @Value("${application.config.plugin}")
    private Boolean enablePlugin;
    
    @Value("${application.config.log}")
    private Boolean logEnable;
    
    @Value("${application.enable.admin-application}")
    private Boolean enableAdminSpringBootApplication;
    
    @Value("${application.enable.n-music-application}")
    private Boolean enableNMusicSpringBootApplication;
    
    @Value("${application.enable.subsonic-application}")
    private Boolean enableSubsonicSpringBootApplication;
    
    @Value("${application.enable.webdav-application}")
    private Boolean webDavSpringBootApplication;
    
    public static Boolean getDebug() {
        return Boolean.TRUE.equals(DEBUG);
    }
    
    @Value("${application.config.log}")
    public void setDebug(Boolean debug) {
        DebugConfig.DEBUG = debug;
    }
    
    private static Boolean empty(Boolean flag) {
        return Boolean.TRUE.equals(flag) ? true : null;
    }
    
    public static Boolean getDebugOption() {
        return empty(Boolean.TRUE.equals(DEBUG));
    }
}
