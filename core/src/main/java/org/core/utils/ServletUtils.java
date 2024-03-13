package org.core.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.URLUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseErrorInfoInterface;
import org.core.common.result.R;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 客户端工具类
 */
public class ServletUtils {
    private ServletUtils() {
    }
    
    /**
     * 获取String参数
     */
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }
    
    /**
     * 获取String参数
     */
    public static String getParameter(String name, String defaultValue) {
        return Convert.toStr(getRequest().getParameter(name), defaultValue);
    }
    
    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name) {
        return Convert.toInt(getRequest().getParameter(name));
    }
    
    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name, Integer defaultValue) {
        return Convert.toInt(getRequest().getParameter(name), defaultValue);
    }
    
    /**
     * 获取Boolean参数
     */
    public static Boolean getParameterToBool(String name) {
        return Convert.toBool(getRequest().getParameter(name));
    }
    
    /**
     * 获取Boolean参数
     */
    public static Boolean getParameterToBool(String name, Boolean defaultValue) {
        return Convert.toBool(getRequest().getParameter(name), defaultValue);
    }
    
    /**
     * 获得所有请求参数
     *
     * @param request 请求对象{@link ServletRequest}
     * @return Map
     */
    public static Map<String, String[]> getParams(ServletRequest request) {
        final Map<String, String[]> map = request.getParameterMap();
        return Collections.unmodifiableMap(map);
    }
    
    /**
     * 获得所有请求参数
     *
     * @param request 请求对象{@link ServletRequest}
     * @return Map
     */
    public static Map<String, String> getParamMap(ServletRequest request) {
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : getParams(request).entrySet()) {
            params.put(entry.getKey(), StringUtils.join(entry.getValue(), ","));
        }
        return params;
    }
    
    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return Objects.requireNonNull(Objects.requireNonNull(getRequestAttributes()).getRequest());
    }
    
    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {
        return Objects.requireNonNull(Objects.requireNonNull(getRequestAttributes()).getResponse());
    }
    
    /**
     * 获取session
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }
    
    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }
    
    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     */
    public static void renderString(HttpServletResponse response, String string) throws IOException {
        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().print(string);
    }
    
    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param error    错误代码
     */
    public static void renderString(HttpServletResponse response, BaseErrorInfoInterface error) throws IOException {
        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().print(new ObjectMapper().writeValueAsString(R.error(error)));
    }
    
    /**
     * 内容编码
     *
     * @param str 内容
     * @return 编码后的内容
     */
    public static String urlEncode(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }
    
    /**
     * 内容解码
     *
     * @param str 内容
     * @return 解码后的内容
     */
    public static String urlDecode(String str) {
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }
    
    public static String paresPath(String path) {
        // 不能使用ServletUtils.getRequest().getRemoteHost(), 因为有时访问localhost,却返回ipv6. 所以直接返回访问域名
        String remoteHost = URLUtil.url(ServletUtils.getRequest().getRequestURL().toString()).getHost();
        int serverPort = ServletUtils.getRequest().getServerPort();
        String scheme = ServletUtils.getRequest().getScheme();
        String s = CharSequenceUtil.removePrefix(path, "/");
        return String.format("%s://%s:%d/%s", scheme, remoteHost, serverPort, s);
    }
}
