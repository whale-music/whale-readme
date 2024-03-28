package org.web.admin.security.filter;

import cn.hutool.http.Header;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.utils.RoleUtil;
import org.core.utils.UserUtil;
import org.core.utils.token.TokenUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.web.admin.security.config.AdminPermitAllUrlProperties;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * token过滤器 验证token有效性，然后注入到线程变量中
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    
    public static final String PREFIX_TOKEN = "Bearer";
    
    private final TokenUtil tokenUtil;
    
    private final AdminPermitAllUrlProperties adminPermitAllUrlProperties;
    
    public JwtAuthenticationTokenFilter(TokenUtil tokenUtil, AdminPermitAllUrlProperties adminPermitAllUrlProperties) {
        this.tokenUtil = tokenUtil;
        this.adminPermitAllUrlProperties = adminPermitAllUrlProperties;
    }
    
    private static void fillAuthUserToken(@NotNull HttpServletRequest request, SysUserPojo userPojo) {
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(userPojo,
                null,
                AuthorityUtils.createAuthorityList(RoleUtil.getRoleNames(userPojo.getRoleName())));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
    
    /**
     * 获取到真实的 token 字符串
     *
     * @param authorizationToken token
     * @return 真实token
     */
    private static String getRealAuthorizationToken(String authorizationToken) {
        return StringUtils.trim(StringUtils.remove(authorizationToken, PREFIX_TOKEN));
    }
    
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String token = getRealAuthorizationToken(request.getHeader(Header.AUTHORIZATION.name()));
        // 如果是匿名接口或者没有token都放行
        Set<String> urls = adminPermitAllUrlProperties.getUrls();
        if (urls.contains(request.getRequestURI()) || StringUtils.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            // 校验token
            tokenUtil.isJwtExpired(token);
            tokenUtil.checkSign(token);
            // 获取用户信息
            SysUserPojo userPojo = tokenUtil.getUserInfo(token);
            // 没有用户则之间跳过
            if (Objects.isNull(userPojo)) {
                filterChain.doFilter(request, response);
                return;
            }
            UserUtil.setUser(userPojo);
            fillAuthUserToken(request, userPojo);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        } finally {
            UserUtil.removeUser();
        }
    }
}
