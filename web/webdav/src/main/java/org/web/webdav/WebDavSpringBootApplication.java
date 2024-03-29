package org.web.webdav;

import org.api.config.ApplicationStartup;
import org.api.config.EnableApiServer;
import org.core.factory.YamlPropertySourceFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@EnableApiServer
@SpringBootApplication
@PropertySource(value = "classpath:application-webdav.yml", factory = YamlPropertySourceFactory.class)
@PropertySource("classpath:application-webdav.properties")
public class WebDavSpringBootApplication implements ApplicationStartup {
}