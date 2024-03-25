package org.web.admin;

import org.api.config.ApplicationStartup;
import org.api.config.EnableApiServer;
import org.core.factory.YamlPropertySourceFactory;
import org.plugin.config.EnablePluginServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnablePluginServer
@EnableApiServer
// 开启安全校验
@EnableWebSecurity
@EnableMethodSecurity
// @EnableAsync
@EntityScan(basePackages = "org.core.jpa.entity")
@EnableJpaRepositories(basePackages = "org.core.jpa.repository")
@PropertySource(value = "classpath:application-admin.yml", factory = YamlPropertySourceFactory.class)
@PropertySource(value = "classpath:application-admin.properties")
@SpringBootApplication
public class AdminSpringBootApplication implements ApplicationStartup {
}
