package org.web.admin.security.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.core.common.annotation.AnonymousAccess;
import org.core.config.WebConfig;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 设置Anonymous注解允许匿名访问的url
 */
@Setter
@Getter
@Configuration
@Slf4j
public class AdminPermitAllUrlProperties implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private final static String asterisk = "*";
    private final static Pattern pattern = Pattern.compile("\\{(.*?)}");
    
    /**
     * 创建并添加本地文件
     */
    private Set<String> urls = new HashSet<>(WebConfig.getPublicList());
    
    /**
     * 获取匿名方法注解
     */
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // 必须用这种方式来获取org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
        // 如果用icon注入方式否则会导致Servlet初始化失败
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        map.keySet().forEach(info -> {
            HandlerMethod handlerMethod = map.get(info);
            
            // 获取方法上边的注解 替代path variable 为 *
            AnonymousAccess method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), AnonymousAccess.class);
            Optional.ofNullable(method)
                    .ifPresent(anonymous -> info.getPatternValues().forEach(url -> urls.add(RegExUtils.replaceAll(url, pattern, asterisk))));
            
            // 获取类上边的注解, 替代path variable 为 *
            AnonymousAccess controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), AnonymousAccess.class);
            Optional.ofNullable(controller).ifPresent(anonymous -> {
                if (Objects.nonNull(info.getPatternsCondition())) {
                    info.getPatternValues().forEach(url -> urls.add(RegExUtils.replaceAll(url, pattern, asterisk)));
                }
            });
        });
    }
}
