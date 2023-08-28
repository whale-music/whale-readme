package org.web.admin;

import org.api.config.ApplicationStartup;
import org.api.config.EnableApiServer;
import org.core.factory.YamlPropertySourceFactory;
import org.plugin.config.EnablePluginServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EntityScan(basePackages = "org.core.jpa.entity")
@EnableJpaRepositories(basePackages = "org.core.jpa.repository")
@EnablePluginServer
@EnableApiServer
@SpringBootApplication
@PropertySource(value = "classpath:application-admin.yml", factory = YamlPropertySourceFactory.class)
@PropertySource(value = "classpath:application-admin.properties")
// 开启安全校验
@EnableWebSecurity
@EnableMethodSecurity
@EnableAsync
public class AdminSpringBootApplication implements ApplicationStartup {
}
