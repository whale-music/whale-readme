package org.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebugConfig {
    private static Boolean debug;
    
    public static Boolean getDebug() {
        return debug;
    }
    
    @Value("${enable.debug}")
    public void setDebug(Boolean debug) {
        DebugConfig.debug = debug;
    }
    
}
