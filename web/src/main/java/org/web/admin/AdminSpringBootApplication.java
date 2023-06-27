package org.web.admin;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"org.web.admin", "org.plugin"})
@PropertySource("classpath:application-admin.properties")
public class AdminSpringBootApplication {
}
