package org.web.webdav.config;

import io.milton.http.annotated.AnnotationResourceFactory;
import io.milton.servlet.MiltonFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web.webdav.controller.WebDavController;


/**
 * This class configure Spring Beans.
 *
 * @author Hossein Fakhraei (HFakhraei@outlook.com)
 * @version 1 14 October 2018
 */
@Configuration
public class SpringBeanConfig {
    
    @Bean
    public FilterRegistrationBean<Filter> someFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new MiltonFilter());
        registration.setName("MiltonFilter");
        registration.addUrlPatterns("/*");
        registration.addInitParameter("resource.factory.class", AnnotationResourceFactory.class.getName());
        registration.addInitParameter("controllerPackagesToScan", WebDavController.class.getPackageName());
        registration.addInitParameter("milton.configurator", MiltonConfig.class.getName());
        registration.setOrder(1);
        return registration;
    }
    
}
