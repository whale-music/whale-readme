package org.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
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
@SpringBootApplication(scanBasePackages = {"org.core", "org.api", "org.oss"}, excludeName = "org.web", exclude = SecurityAutoConfiguration.class)
@EnableAsync // 开启任务
@EnableScheduling // 开启定时任务
public class MusicBoxSpringBoot {
    public static void main(String[] args) {
        // AdminSpringBootApplication需要最后，方便执行run方法。
        // 这样ApplicationRunner和CommandLineRunner这些类似的方法才会接受到参数, 而且也只会在Admin包下的包才会读取到args
        new SpringApplicationBuilder(MusicBoxSpringBoot.class)
                .child(SubsonicSpringBootApplication.class)
                .sibling(NeteaseCloudMusicSpringBootApplication.class)
                .sibling(AdminSpringBootApplication.class)
                .run(args);
    }
}