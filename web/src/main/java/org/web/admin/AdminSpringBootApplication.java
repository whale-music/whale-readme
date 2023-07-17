package org.web.admin;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(scanBasePackages = {"org.web.admin", "org.plugin"}, proxyBeanMethods = false)
@PropertySource("classpath:application-admin.properties")
// 开启安全校验
@EnableWebSecurity
@EnableMethodSecurity
public class AdminSpringBootApplication {
}
