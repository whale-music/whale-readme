package org.web.webdav.config;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.authenticator.BasicAuthenticator;
import org.apache.tomcat.util.descriptor.web.LoginConfig;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.api.webdav.service.WebdavCacheApi;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web.webdav.auth.WebdavRealm;
import org.web.webdav.tomcat.WebdavResourceSet;
import org.web.webdav.tomcat.WebdavStandardRoot;
import org.web.webdav.tomcat.servlet.ExtendedWebdavServlet;
import org.web.webdav.tomcat.servlet.WebdavServlet;


@Configuration
@RequiredArgsConstructor
public class WebdavConfig {
    
    private final WebdavCacheApi webdavCacheApi;
    
    private final WebdavRealm webdavRealm;
    
    @Bean
    public ServletRegistrationBean<WebdavServlet> webdavServlet() {
        ExtendedWebdavServlet webdavServlet = new ExtendedWebdavServlet();
        ServletRegistrationBean<WebdavServlet> registration = new ServletRegistrationBean<>(webdavServlet, "/dav/*");
        registration.setName("WebDAV servlet");
        registration.setServlet(webdavServlet);
        registration.setLoadOnStartup(-1);
        // 是否启动webdav, false则返回错误
        registration.addInitParameter("listings", String.valueOf(true));
        // 设置数据是否只读
        registration.addInitParameter("readonly", String.valueOf(true));
        // 设置日志打印等级
        registration.addInitParameter("debug", String.valueOf(0));
        return registration;
    }
    
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> factory.addContextCustomizers(context -> {
            // 创建一个新的WebResourceRoot实例
            WebdavStandardRoot resources = new WebdavStandardRoot(context);
            resources.addPreResources(new WebdavResourceSet(resources, "/", webdavCacheApi));
            context.setResources(resources);
            // 添加Webdav用户身份校验
            context.getPipeline().addValve(new BasicAuthenticator());
            // 用户身份验证和授权
            context.setRealm(webdavRealm);
            
            // 设置安全约束
            SecurityConstraint securityConstraint = new SecurityConstraint();
            securityConstraint.addAuthRole("webdav");
            
            SecurityCollection securityCollection = new SecurityCollection();
            securityCollection.addPattern("/dav/*");
            securityConstraint.addCollection(securityCollection);
            context.addConstraint(securityConstraint);
            
            // 设置登录配置
            LoginConfig loginConfig = new LoginConfig();
            loginConfig.setAuthMethod("BASIC");
            context.setLoginConfig(loginConfig);
            
        });
    }
    
}
