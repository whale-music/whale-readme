package org.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {
    
    public static String SEED_KEY;
    
    public static Long EXPIRE_TIME;
    
    
    private JwtConfig() {
    }
    
    /**
     * 设置
     */
    @Value("${jwt-config.seed-key}")
    public void setSeedKey(String seedKey) {
        JwtConfig.SEED_KEY = seedKey;
    }
    
    /**
     * 设置
     */
    @Value("${jwt-config.expire-time}")
    public void setExpireTime(Long expireTime) {
        JwtConfig.EXPIRE_TIME = expireTime;
    }
}
