package org.starter;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.extra.spring.EnableSpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.api.config.ApplicationStartup;
import org.core.common.properties.DebugConfig;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

// 启用Hutool Spring
@EnableSpringUtil
@EntityScan(basePackages = "org.core.jpa.entity")
@EnableJpaRepositories(basePackages = "org.core.jpa.repository")
// @SpringBootApplication(scanBasePackages = {"org.core", "org.api", "org.oss"}, excludeName = "org.web", exclude = SecurityAutoConfiguration.class)
@SpringBootApplication(scanBasePackages = "org.core")
@EnableScheduling // 开启定时任务
@Slf4j
public class StartSpringBoot {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException, ClassNotFoundException {
        StopWatch sw = new StopWatch("priming step");
        sw.start("StartSpringBoot start");
        SpringApplicationBuilder build = new SpringApplicationBuilder(StartSpringBoot.class);
        ConfigurableApplicationContext context = build.web(WebApplicationType.NONE).run(args);
        sw.stop();
        
        sw.start("Scan class SpringBootApplication annotation");
        Set<Class<?>> org = searchClassesWithAnnotation("org.web", SpringBootApplication.class);
        sw.stop();
        for (Class<?> aClass : org) {
            sw.start(aClass.getSimpleName());
            Object o = aClass.getDeclaredConstructor().newInstance();
            String simpleName = CharSequenceUtil.toSymbolCase(aClass.getSimpleName(), '-');
            String property = context.getEnvironment().getProperty("application.enable." + simpleName);
            if (Boolean.parseBoolean(property)) {
                if (o instanceof ApplicationStartup startup) {
                    startup.start(build, args);
                } else {
                    throw new NoSuchMethodException();
                }
            }
            sw.stop();
        }
        if (Boolean.TRUE.equals(DebugConfig.getDebug())) {
            log.info("\n" + sw.prettyPrint(TimeUnit.MILLISECONDS));
        }
        File file = ResourceUtils.getFile("classpath:start_banner");
        log.info(UnicodeUtil.toString(FileUtils.readFileToString(file, StandardCharsets.UTF_8)));
    }
    
    
    public static Set<Class<?>> searchClassesWithAnnotation(String basePackage, Class<? extends Annotation> annotation) throws IOException, ClassNotFoundException {
        Set<Class<?>> classes = new TreeSet<>(Comparator.comparing(Class::getSimpleName));
        // spring工具类，可以获取指定路径下的全部类
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(basePackage) + "/**/*.class";
        Resource[] resources = resourcePatternResolver.getResources(pattern);
        // MetadataReader 的工厂类
        MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        for (Resource resource : resources) {
            // 用于读取类信息
            MetadataReader reader = readerFactory.getMetadataReader(resource);
            // 扫描到的class
            String classname = reader.getClassMetadata().getClassName();
            Class<?> clazz = Class.forName(classname);
            // 判断是否有指定主解
            Annotation anno = clazz.getAnnotation(annotation);
            if (anno != null) {
                classes.add(clazz);
            }
        }
        return classes;
    }
}