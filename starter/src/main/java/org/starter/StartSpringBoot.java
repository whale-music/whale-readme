package org.starter;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.extra.spring.EnableSpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.config.ApplicationStartup;
import org.core.common.annotation.StartBootName;
import org.core.common.properties.DebugConfig;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.web.admin.AdminSpringBootApplication;
import org.web.nmusic.NMusicSpringBootApplication;
import org.web.subsonic.SubsonicSpringBootApplication;
import org.web.webdav.WebDavSpringBootApplication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
// 开启缓存注解
@EnableCaching
// 开启异步注解
@EnableAsync
// 开启定时任务
@EnableScheduling
// Spring 工具类
@EnableSpringUtil
@EntityScan(basePackages = "org.core.jpa.entity")
@EnableJpaRepositories(basePackages = "org.core.jpa.repository")
@SpringBootApplication(scanBasePackages = "org.core")
public class StartSpringBoot {
    
    private static final ApplicationStartup[] STARTUPS = new ApplicationStartup[]{
            new AdminSpringBootApplication(),
            new NMusicSpringBootApplication(),
            new SubsonicSpringBootApplication(),
            new WebDavSpringBootApplication()
    };
    
    public static void main(String[] args) throws IOException {
        StopWatch sw = new StopWatch("priming step");
        sw.start("StartSpringBoot start");
        SpringApplicationBuilder build = new SpringApplicationBuilder(StartSpringBoot.class);
        ConfigurableApplicationContext context = build.web(WebApplicationType.NONE).run(args);
        sw.stop();
        
        for (ApplicationStartup startup : STARTUPS) {
            Class<? extends ApplicationStartup> aClass = startup.getClass();
            sw.start(aClass.getSimpleName());
            // 判断是否启动对应类，获取配置与启动类名对应。如果配置没有生效，请先检查类名与配置是否对应
            StartBootName annotation = aClass.getAnnotation(StartBootName.class);
            String startBootName = annotation.value();
            String property = context.getEnvironment().getProperty("application.enable." + startBootName);
            if (Boolean.parseBoolean(property)) {
                startup.start(build, args);
            } else {
                log.info("{} no start", startBootName);
            }
            sw.stop();
        }
        if (Boolean.TRUE.equals(DebugConfig.getDebug())) {
            log.info("\n" + sw.prettyPrint(TimeUnit.MILLISECONDS));
        }
        String startBanner = ResourceUtil.readUtf8Str("start_banner");
        log.info(UnicodeUtil.toString(startBanner));
    }
    
}