package org.web.subsonic;

import org.api.config.ApplicationStartup;
import org.api.config.EnableApiServer;
import org.core.factory.YamlPropertySourceFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;


@EnableApiServer
@SpringBootApplication
@PropertySource(value = "classpath:application-subsonic.yml", factory = YamlPropertySourceFactory.class)
@PropertySource("classpath:application-subsonic.properties")
public class SubsonicSpringBootApplication implements ApplicationStartup {
}
