package org.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"org.api", "org.core"})
public class ApiAutoConfiguration {
}
