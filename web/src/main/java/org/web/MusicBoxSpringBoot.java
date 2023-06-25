package org.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

// jpa
@EntityScan(basePackages = "org.core.jpa.entity")
@EnableJpaRepositories(basePackages = "org.core.jpa.repository")
@SpringBootApplication(scanBasePackages = {"org.core", "org.web", "org.api", "org.oss", "org.plugin"})
@EnableAsync // 开启任务
@EnableScheduling // 开启定时任务
public class MusicBoxSpringBoot {
    public static void main(String[] args) {
        SpringApplication.run(MusicBoxSpringBoot.class, args);
    }
}