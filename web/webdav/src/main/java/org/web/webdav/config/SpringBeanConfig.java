package org.web.webdav.config;

import io.milton.http.annotated.AnnotationResourceFactory;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * This class configure Spring Beans.
 *
 * @author Hossein Fakhraei (HFakhraei@outlook.com)
 * @version 1 14 October 2018
 */
@Configuration
public class SpringBeanConfig {
    
    /**
     * 注入Webdav过滤器, 让所有请求都走Webdav的过滤器
     *
     * @param webdavFilter Webdav过滤器
     * @return 过滤器
     */
    @Bean
    public FilterRegistrationBean<Filter> someFilterRegistration(WebdavFilter webdavFilter) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(webdavFilter);
        registration.setName("MiltonFilter");
        registration.addUrlPatterns("/*");
        // 作为Milton初始化的类
        registration.addInitParameter("resource.factory.class", AnnotationResourceFactory.class.getName());
        // 注册一个空的bean, milton会自动创建bean.但是不需要它创建. 所以没有这个会抛出异常
        registration.addInitParameter("contextConfigClass", Object.class.getName());
        // registration.addInitParameter("controllerPackagesToScan", WebDavController.class.getPackageName());
        // 不使用默认new创建类，使用spring boot注入该Bean
        // registration.addInitParameter("milton.configurator", MiltonConfig.class.getName());
        // registration.setOrder(1);
        return registration;
    }
    
}
