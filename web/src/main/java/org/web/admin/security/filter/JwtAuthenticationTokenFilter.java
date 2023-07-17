package org.web.admin.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.core.common.constant.VerifyAuthIdentifierConstant;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.utils.TokenUtil;
import org.core.utils.UserUtil;
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
    
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(VerifyAuthIdentifierConstant.ADMIN_VERIFY_AUTH_IDENTIFIER);
        if (StringUtils.isNotBlank(token)) {
            TokenUtil.checkSign(token);
            // 校验token, 并获取信息
            SysUserPojo userPojo = TokenUtil.getInfo(token);
            if (Objects.nonNull(userPojo)) {
                UserUtil.setUser(userPojo);
                UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(userPojo,
                        null,
                        AuthorityUtils.createAuthorityList(userPojo.getAccountType() == 0 ? "admin" : "common"));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
        UserUtil.removeUser();
    }
}
