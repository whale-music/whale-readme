package org.core.config;

import cn.hutool.core.io.FileUtil;
import org.apache.commons.lang3.StringUtils;
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
        if (StringUtils.equalsIgnoreCase(saveConfig.getSaveMode(), "Local") && !registry.hasMappingForPattern(PUBLIC_URL)) {
            String replace = StringUtils.replace(saveConfig.getHost(), "/", FileUtil.FILE_SEPARATOR);
            String host = StringUtils.replace(replace, "\\", FileUtil.FILE_SEPARATOR);
            saveConfig.setHost(host);
            // 映射本地文件夹路径时，结尾必须以"\ or \\"结尾, 否则访问404
            if (!StringUtils.endsWith(saveConfig.getHost(), FileUtil.FILE_SEPARATOR)) {
                saveConfig.setHost(saveConfig.getHost() + FileUtil.FILE_SEPARATOR);
            }
            registry.addResourceHandler(PUBLIC_URL)
                    .addResourceLocations("file:" + saveConfig.getHost());
        }
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
