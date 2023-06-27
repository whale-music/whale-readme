package org.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.web.admin.AdminSpringBootApplication;
import org.web.neteasecloudmusic.NeteaseCloudMusicSpringBootApplication;
import org.web.subsonic.SubsonicSpringBootApplication;

// jpa
@EntityScan(basePackages = "org.core.jpa.entity")
@EnableJpaRepositories(basePackages = "org.core.jpa.repository")
@SpringBootApplication(scanBasePackages = {"org.core", "org.api", "org.oss"}, excludeName = "org.web")
@EnableAsync // 开启任务
@EnableScheduling // 开启定时任务
public class MusicBoxSpringBoot {
    public static void main(String[] args) {
        new SpringApplicationBuilder(MusicBoxSpringBoot.class)
                .child(AdminSpringBootApplication.class)
                .sibling(NeteaseCloudMusicSpringBootApplication.class)
                .sibling(SubsonicSpringBootApplication.class)
                .run(args);
    }
}