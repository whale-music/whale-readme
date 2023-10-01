package org.core.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 手动创建 TaskExecutor. 防止使用自定义配置占用太多内存
 */
@Configuration
// @EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    
    public static final String PUBLIC_URL = "/common/static/**";
    
    private final SaveConfig saveConfig;
    
    public WebConfig(SaveConfig saveConfig) {
        this.saveConfig = saveConfig;
    }
    
    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler(PUBLIC_URL)
                .addResourceLocations("file:" + saveConfig.getHost());
    }
    
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(taskExecutor());
    }
    
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(10);
        // 设置线程最大数量
        executor.setMaxPoolSize(50);
        // 队列容量
        executor.setQueueCapacity(25);
        return executor;
    }
}
