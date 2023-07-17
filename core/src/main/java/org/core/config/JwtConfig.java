package org.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    
    private static String seedKey = "";
    
    private static Long expireTime = 0L;
    
    
    public static String getSeedKey() {
        return seedKey;
    }
    
    /**
     * 设置
     */
    @Value("${jwt-config.seed-key}")
    public void setSeedKey(String seedKey) {
        JwtConfig.seedKey = seedKey;
    }
    
    public static Long getExpireTime() {
        return expireTime;
    }
    
    /**
     * 设置
     */
    @Value("${jwt-config.expire-time}")
    public void setExpireTime(Long expireTime) {
        JwtConfig.expireTime = expireTime;
    }
}
