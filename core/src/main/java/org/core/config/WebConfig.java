package org.core.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import org.apache.commons.lang3.StringUtils;
import org.core.common.enums.SaveModeEnum;
import org.core.common.properties.SaveConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    public static final String WEB_VERSION = "/web/**";
    public static final String PUBLIC_STATIC_URL = "/common/static/**";
    public static final String PUBLIC_ASSETS_URL = "/assets/**";
    public static final String PUBLIC_LOGIN_KEY_URL = "/login-key/**";
    
    public static final String API_DOCS = "/v3/api-docs/**";
    public static final String KNIFE4J_API_DOCS = "/docs/**";
    public static final String SWAGGER_UI = "/swagger-ui/**";
    
    private static final List<String> PUBLIC_LIST = new ArrayList<>();
    
    static {
        PUBLIC_LIST.add(WEB_VERSION);
        PUBLIC_LIST.add(PUBLIC_STATIC_URL);
        PUBLIC_LIST.add(PUBLIC_ASSETS_URL);
        PUBLIC_LIST.add(PUBLIC_LOGIN_KEY_URL);
        PUBLIC_LIST.add(API_DOCS);
        PUBLIC_LIST.add(KNIFE4J_API_DOCS);
        PUBLIC_LIST.add(SWAGGER_UI);
    }
    
    private final SaveConfig saveConfig;
    
    public WebConfig(SaveConfig saveConfig) {
        this.saveConfig = saveConfig;
    }
    
    public static List<String> getPublicList() {
        return PUBLIC_LIST;
    }
    
    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        if (Objects.equals(saveConfig.getSaveMode(), SaveModeEnum.LOCAL) && !registry.hasMappingForPattern(PUBLIC_STATIC_URL)) {
            String replace = StringUtils.replace(saveConfig.getHost(), "/", FileUtil.FILE_SEPARATOR);
            String host = StringUtils.replace(replace, "\\", FileUtil.FILE_SEPARATOR);
            saveConfig.setHost(host);
            // 映射本地文件夹路径时，结尾必须以"\ or \\"结尾, 否则访问404
            registry.addResourceHandler(PUBLIC_STATIC_URL)
                    .addResourceLocations("file:" + CharSequenceUtil.addSuffixIfNot(saveConfig.getHost(), FileUtil.FILE_SEPARATOR));
        }
    }
    
    /**
     * 配置 "全局 "跨源请求处理。配置的 CORS
     * 映射适用于注释控制器、功能端点和静态
     * 资源。
     * <p> 注解控制器可通过以下方式进一步声明更精细的配置
     * {@link CrossOrigin @CrossOrigin}
     * 在这种情况下，此处声明的 "全局 "CORS 配置为
     * {@link CorsConfiguration#combine(CorsConfiguration) combined} * 与在控制器上定义的本地 CORS 配置相结合。
     * 与控制器方法上定义的本地 CORS 配置相结合。
     *
     * @see CorsRegistry
     * @see CorsConfiguration#combine(CorsConfiguration)
     * @since 4.2
     */
    @Override
    public void addCorsMappings(@NotNull CorsRegistry registry) {
        // 允许音频文件跨域
        for (String s : getPublicList()) {
            registry.addMapping(s) // 映射到静态资源的路径
                    .allowedOriginPatterns("*") // 允许来自此来源的跨域请求
                    .allowedMethods("*") // 允许的HTTP方法
                    .allowCredentials(true); // 允许跨域请求携带凭据（如Cookie）
        }
    }
    
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(taskExecutor());
    }
    
    /**
     * 手动创建 TaskExecutor. 防止使用自定义配置占用太多内存
     *
     * @return ThreadPoolTaskExecutor
     */
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
