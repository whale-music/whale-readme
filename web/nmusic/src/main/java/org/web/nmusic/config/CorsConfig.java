package org.web.nmusic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * 根据配置文件cross是否开启跨域
 */
@Configuration
@ConditionalOnProperty(value = "cross.nmusic.enable", havingValue = "true", matchIfMissing = true)
public class CorsConfig {
    
    @Bean
    public CorsFilter corsFilter(@Value("${cross.nmusic.domains}") List<String> domains) {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        
        // 添加需要跨域域名
        domains.forEach(config::addAllowedOrigin);
        // config.addAllowedOrigin("http://localhost:8081/");
        // config.addAllowedOrigin("http://localhost:8848/");
        config.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
