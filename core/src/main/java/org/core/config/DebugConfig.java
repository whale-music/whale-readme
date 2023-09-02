package org.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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
    
    public static Boolean getDebug() {
        return DEBUG;
    }
    
    @Value("${application.config.log}")
    public void setDebug(Boolean debug) {
        DebugConfig.DEBUG = debug;
    }
    
    public Boolean getEnablePlugin() {
        return enablePlugin;
    }
    
    public void setEnablePlugin(Boolean enablePlugin) {
        this.enablePlugin = enablePlugin;
    }
    
    public boolean isLogEnable() {
        return logEnable;
    }
    
    public void setLogEnable(boolean logEnable) {
        this.logEnable = logEnable;
    }
    
    public boolean isEnableAdminSpringBootApplication() {
        return enableAdminSpringBootApplication;
    }
    
    public void setEnableAdminSpringBootApplication(boolean enableAdminSpringBootApplication) {
        this.enableAdminSpringBootApplication = enableAdminSpringBootApplication;
    }
    
    public boolean isEnableNeteaseCloudMusicSpringBootApplication() {
        return enableNeteaseCloudMusicSpringBootApplication;
    }
    
    public void setEnableNeteaseCloudMusicSpringBootApplication(boolean enableNeteaseCloudMusicSpringBootApplication) {
        this.enableNeteaseCloudMusicSpringBootApplication = enableNeteaseCloudMusicSpringBootApplication;
    }
    
    public boolean isEnableSubsonicSpringBootApplication() {
        return enableSubsonicSpringBootApplication;
    }
    
    public void setEnableSubsonicSpringBootApplication(boolean enableSubsonicSpringBootApplication) {
        this.enableSubsonicSpringBootApplication = enableSubsonicSpringBootApplication;
    }
}
