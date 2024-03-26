package org.web.nmusic;

import org.api.config.ApplicationStartup;
import org.api.config.EnableApiServer;
import org.core.factory.YamlPropertySourceFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableApiServer
// 开启安全校验
@EnableWebSecurity
@EnableMethodSecurity
@PropertySource(value = "classpath:application-nmusic.yml", factory = YamlPropertySourceFactory.class)
@PropertySource("classpath:application-nmusic.properties")
@SpringBootApplication
public class NMusicSpringBootApplication implements ApplicationStartup {
}
