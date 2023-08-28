package org.starter;

import cn.hutool.core.text.CharSequenceUtil;
import org.api.config.ApplicationStartup;
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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

@EntityScan(basePackages = "org.core.jpa.entity")
@EnableJpaRepositories(basePackages = "org.core.jpa.repository")
// @SpringBootApplication(scanBasePackages = {"org.core", "org.api", "org.oss"}, excludeName = "org.web", exclude = SecurityAutoConfiguration.class)
@SpringBootApplication(scanBasePackages = "org.core")
@EnableScheduling // 开启定时任务
public class StartSpringBoot {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException, ClassNotFoundException {
        SpringApplicationBuilder build = new SpringApplicationBuilder(StartSpringBoot.class);
        ConfigurableApplicationContext context = build.web(WebApplicationType.NONE).run(args);
        Set<Class<?>> org = searchClassesWithAnnotation("org.web", SpringBootApplication.class);
        for (Class<?> aClass : org) {
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
        }
    }
    
    
    public static Set<Class<?>> searchClassesWithAnnotation(String basePackage, Class<? extends Annotation> annotation) throws IOException, ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
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