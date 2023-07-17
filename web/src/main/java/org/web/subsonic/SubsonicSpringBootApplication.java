package org.web.subsonic;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

/**
 * 暂时不需要Spring Security 配置
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class, proxyBeanMethods = false)
@PropertySource("classpath:application-subsonic.properties")
public class SubsonicSpringBootApplication {
}
