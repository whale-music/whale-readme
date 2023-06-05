package org.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"org.core", "org.web", "org.api", "org.oss", "org.plugin"})
@EnableAsync
public class MusicBoxSpringBoot {
    public static void main(String[] args) {
        SpringApplication.run(MusicBoxSpringBoot.class, args);
    }
}