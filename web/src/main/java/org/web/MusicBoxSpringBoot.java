package org.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.core", "org.web", "org.api", "org.oss"})
public class MusicBoxSpringBoot {
    public static void main(String[] args) {
        SpringApplication.run(MusicBoxSpringBoot.class, args);
    }
}