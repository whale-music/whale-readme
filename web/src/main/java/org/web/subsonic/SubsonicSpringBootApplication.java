package org.web.subsonic;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application-subsonic.properties")
public class SubsonicSpringBootApplication {
}
