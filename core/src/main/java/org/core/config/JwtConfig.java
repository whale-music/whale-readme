package org.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt-config")
public class JwtConfig {
    // 密钥
    private String seedKey = "";
    //  单位毫秒
    private Long expireTime = 0L;
    // 刷新密钥过期时间
    private Long refreshExpireTime = 0L;
}
