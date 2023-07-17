package org.web.neteasecloudmusic;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(proxyBeanMethods = false)
@PropertySource("classpath:application-neteasecloudmusic.properties")
public class NeteaseCloudMusicSpringBootApplication {
}
