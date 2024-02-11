package org.web.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.web.admin.security.config.AdminPermitAllUrlProperties;
import org.web.admin.security.filter.JwtAuthenticationTokenFilter;
import org.web.admin.security.handle.AnonymousAuthenticationEntryPoint;

import java.util.Set;

@Component
public class SecurityConfig {
    
    /**
     * 匿名用户访问异常
     */
    private final AnonymousAuthenticationEntryPoint authenticationEntryPoint;
    
    /**
     * 访问token认证
     */
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    
    /**
     * 获取匿名访问的url
     */
    private final AdminPermitAllUrlProperties adminPermitAllUrlProperties;
    
    public SecurityConfig(AnonymousAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter, AdminPermitAllUrlProperties adminPermitAllUrlProperties) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.adminPermitAllUrlProperties = adminPermitAllUrlProperties;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        Set<String> passUrls = adminPermitAllUrlProperties.getUrls();
        
        http.csrf(AbstractHttpConfigurer::disable);
        // 将我们的JWT filter添加到UsernamePasswordAuthenticationFilter前面，因为这个Filter是authentication开始的filter，我们要早于它
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        
        http.authorizeHttpRequests(authorize ->
                authorize
                        // 登录放行
                        .requestMatchers(passUrls.toArray(new String[]{})).permitAll()
                        // 忽略静态资源
                        .requestMatchers(
                                "/",
                                "/web/assets/**",
                                "/web/static/**",
                                "/web/favicon.ico",
                                "/web/index.html",
                                "/web/logo.svg",
                                "/web/serverConfig.json"
                        ).permitAll()
                        // 其他的所有都需要认证
                        .anyRequest().authenticated());
        // 异常处理(权限拒绝、登录失效等)
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                // 匿名用户访问无权限资源时的异常处理
                httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint)
        );
        // 基于token，所以不需要session
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));
        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // 设置角色的默认前缀为 "ROLE_"
        return new GrantedAuthorityDefaults("ROLE_");
    }
}
