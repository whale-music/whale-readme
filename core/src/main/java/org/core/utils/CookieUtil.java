package org.core.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;

public class CookieUtil {
    private CookieUtil() {
    }
    
    public static Cookie createCookieString(String name, String value, int maxAge, String expires, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setHttpOnly(true);
        // 设置Expires属性
        cookie.setAttribute("Expires", expires);
        return cookie;
    }
    
    public static String cookieToString(Cookie[] cookies, HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        for (Cookie cookie : cookies) {
            String s = new Rfc6265CookieProcessor().generateHeader(cookie, req);
            sb.append(s).append(";");
        }
        return sb.toString();
    }
}
