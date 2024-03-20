package org.web.subsonic;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.api.config.ApplicationStartup;
import org.api.config.EnableApiServer;
import org.core.factory.YamlPropertySourceFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;


@OpenAPIDefinition(
        info = @Info(
                title = "Subsonic",
                version = "1.0.0",
                description = "Subsonic API"
        )
)
@EnableApiServer
@SpringBootApplication
@PropertySource(value = "classpath:application-subsonic.yml", factory = YamlPropertySourceFactory.class)
@PropertySource("classpath:application-subsonic.properties")
@EnableCaching
public class SubsonicSpringBootApplication implements ApplicationStartup {
}
