package org.web.nmusic.security.filter;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.core.common.constant.CookieConstant;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.utils.RoleUtil;
import org.core.utils.UserUtil;
import org.core.utils.token.TokenUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.web.nmusic.security.config.NeteaseCloudMusicPermitAllUrlProperties;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * token过滤器 验证token有效性，然后注入到线程变量中
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    
    private final NeteaseCloudMusicPermitAllUrlProperties permitAllUrlProperties;
    
    private final TokenUtil tokenUtil;
    
    public JwtAuthenticationTokenFilter(NeteaseCloudMusicPermitAllUrlProperties permitAllUrlProperties, TokenUtil tokenUtil) {
        this.permitAllUrlProperties = permitAllUrlProperties;
        this.tokenUtil = tokenUtil;
    }
    
    private HttpServletRequest requestWrapper;
    
    private static String getCookiesByParam(@NotNull HttpServletRequest request) {
        try {
            return request.getParameterMap().get("cookie")[0];
        } catch (Exception ignored) {
            return null;
        }
    }
    
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        // 如果要从post body中获取token, 则使用自定义 RepeatedlyRequestWrapper, 缓存request body中的数据
        requestWrapper = request;
        // 放行运行匿名访问的地址
        String token = getToken();
        if (mathUri(requestWrapper)) {
            // 如果是匿名接口，校验失败不抛出异常
            if (validate(token)) {
                setSecurityUser(requestWrapper, token);
            }
            filterChain.doFilter(requestWrapper, response);
        } else {
            try {
                if (StringUtils.isNotBlank(token)) {
                    tokenUtil.checkSign(token);
                    setSecurityUser(requestWrapper, token);
                }
                filterChain.doFilter(requestWrapper, response);
            } finally {
                UserUtil.removeUser();
            }
        }
    }
    
    /**
     * 从cookie header parameter body 中获取token
     *
     * @return token
     */
    @Nullable
    private String getToken() {
        // 获取cookie
        Optional<String> first = Arrays.stream(requestWrapper.getCookies() == null ? new Cookie[]{} : requestWrapper.getCookies())
                                       .filter(cookie -> StringUtils.equalsIgnoreCase(CookieConstant.COOKIE_NAME_MUSIC_U, cookie.getName()))
                                       .map(Cookie::getValue)
                                       .filter(StringUtils::isNotBlank)
                                       .findFirst();
        // cookie中没有则从其他参数中获取
        String token = null;
        if (first.isPresent()) {
            token = first.get();
        } else {
            try {
                // get parameters
                String cookies = getCookiesByParam(requestWrapper);
                if (StringUtils.isBlank(cookies)) {
                    // 如果需要从post body中取数据，则使用缓存。否则后续读取post body会报错。因为流只能读取一次
                    this.requestWrapper = new RepeatedlyRequestWrapper(requestWrapper);
                    // post body
                    String jsonBody = IOUtils.toString(requestWrapper.getReader());
                    cookies = JSONUtil.parseObj(jsonBody).get(CookieConstant.COOKIE_NAME_COOKIE, String.class);
                }
                token = parserCookie(cookies);
            } catch (Exception e) {
                log.error("Failed to parse cookie, origin msgs: {}", e.getMessage());
                log.error("error path: {}", requestWrapper.getRequestURI());
            }
        }
        return token;
    }
    
    private static String parserCookie(String cookies) {
        for (String cookie : StringUtils.split(cookies, ";")) {
            String[] split = StringUtils.split(cookie, "=");
            if (split.length == 2 && StringUtils.equals(split[0], CookieConstant.COOKIE_NAME_MUSIC_U)) {
                return split[1];
            }
        }
        return null;
    }
    
    private void setSecurityUser(@NotNull HttpServletRequest request, String token) {
        // 校验token, 并获取信息
        SysUserPojo userPojo = tokenUtil.getUserInfo(token);
        if (Objects.nonNull(userPojo)) {
            UserUtil.setUser(userPojo);
            UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(userPojo,
                    null,
                    AuthorityUtils.createAuthorityList(RoleUtil.getRoleNames(userPojo.getRoleName())));
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }
    
    private boolean mathUri(@NotNull HttpServletRequest request) {
        return permitAllUrlProperties.getUrls().contains(request.getRequestURI())
                || PatternMatchUtils.simpleMatch(permitAllUrlProperties.getUrls().toArray(String[]::new), request.getRequestURI());
    }
    
    /**
     * 成功 true 失败false
     *
     * @param token token
     * @return flag
     */
    private boolean validate(String token) {
        if (StringUtils.isBlank(token)) {
            return false;
        }
        try {
            tokenUtil.checkSign(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
