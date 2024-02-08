package org.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    
    private static String seedKey = "";
    //  单位毫秒
    private static Long expireTime = 0L;
    private static Long refreshExpireTime = 0L;
    
    
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
    
    public static Long getRefreshExpireTime() {
        return refreshExpireTime;
    }
    
    /**
     * 设置refresh token时间戳
     */
    @Value("${jwt-config.refresh-key}")
    public void setRefreshExpireTime(Long expireTime) {
        JwtConfig.refreshExpireTime = expireTime;
    }
    
    /**
     * 设置token时间戳
     */
    @Value("${jwt-config.expire-time}")
    public void setExpireTime(Long expireTime) {
        JwtConfig.expireTime = expireTime;
    }
}
