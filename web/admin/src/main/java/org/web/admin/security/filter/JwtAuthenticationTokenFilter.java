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

import java.io.IOException;
import java.util.Objects;

/**
 * token过滤器 验证token有效性，然后注入到线程变量中
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    
    public static final String PREFIX_TOKEN = "Bearer";
    
    private final TokenUtil tokenUtil;
    
    public JwtAuthenticationTokenFilter(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }
    
    /**
     * 获取到真实的 token 串
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
        if (StringUtils.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            tokenUtil.isJwtExpired(token);
            tokenUtil.checkSign(token);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // 校验token, 并获取信息
        SysUserPojo userPojo = tokenUtil.getUserInfo(token);
        if (Objects.isNull(userPojo)) {
            filterChain.doFilter(request, response);
            return;
        }
        UserUtil.setUser(userPojo);
        try {
            UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(userPojo,
                    null,
                    AuthorityUtils.createAuthorityList(RoleUtil.getRoleNames(userPojo.getRoleName())));
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } finally {
            UserUtil.removeUser();
        }
    }
}
