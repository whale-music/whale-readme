package org.web.neteasecloudmusic.security.config;

import org.apache.commons.lang3.RegExUtils;
import org.core.common.annotation.AnonymousAccess;
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
@Configuration
public class NeteaseCloudMusicPermitAllUrlProperties {
    
    private Set<String> urls = new HashSet<>();
    
    /**
     * 获取匿名方法注解
     */
    public Set<String> getRequestMappingUrls(RequestMappingHandlerMapping mapping) {
        String asterisk = "*";
        Pattern pattern = Pattern.compile("\\{(.*?)}");
        
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
        setUrls(urls);
        return urls;
    }
    
    public Set<String> getUrls() {
        return urls;
    }
    
    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }
}
