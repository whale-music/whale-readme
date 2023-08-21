package org.web.neteasecloudmusic;

import org.api.config.ApplicationStartup;
import org.api.config.EnableApiServer;
import org.core.factory.YamlPropertySourceFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@EnableApiServer
@PropertySource(value = "classpath:application-neteasecloudmusic.yml", factory = YamlPropertySourceFactory.class)
@PropertySource("classpath:application-neteasecloudmusic.properties")
@SpringBootApplication
public class NeteaseCloudMusicSpringBootApplication implements ApplicationStartup {
}
