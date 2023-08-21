package org.plugin.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.plugin")
@ConditionalOnProperty(value = "application.config.plugin", havingValue = "true")
public class PluginAutoConfiguration {
}
