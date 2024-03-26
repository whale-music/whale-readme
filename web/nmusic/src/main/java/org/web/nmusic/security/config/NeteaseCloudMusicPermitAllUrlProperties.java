package org.web.nmusic.security.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RegExUtils;
import org.core.common.annotation.AnonymousAccess;
import org.core.config.WebConfig;
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
public class NeteaseCloudMusicPermitAllUrlProperties  {
    private final static String asterisk = "*";
    private final static Pattern pattern = Pattern.compile("\\{(.*?)}");
    
    private Set<String> urls = new HashSet<>(WebConfig.getPublicList());
    
    
    public String[] getArrayUrls(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
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
        
        return urls.toArray(new String[]{});
    }
}
