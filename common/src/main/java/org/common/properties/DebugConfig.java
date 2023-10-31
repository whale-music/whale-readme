package org.common.properties;

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
    
    @Value("${application.enable.admin-spring-boot-application}")
    private Boolean enableAdminSpringBootApplication;
    
    @Value("${application.enable.netease-cloud-music-spring-boot-application}")
    private Boolean enableNeteaseCloudMusicSpringBootApplication;
    
    @Value("${application.enable.subsonic-spring-boot-application}")
    private Boolean enableSubsonicSpringBootApplication;
    
    @Value("${application.enable.web-dav-spring-boot-application}")
    private Boolean webDavSpringBootApplication;
    
    public static Boolean getDebug() {
        return Boolean.TRUE.equals(DEBUG);
    }
    
    @Value("${application.config.log}")
    public void setDebug(Boolean debug) {
        DebugConfig.DEBUG = debug;
    }
}
