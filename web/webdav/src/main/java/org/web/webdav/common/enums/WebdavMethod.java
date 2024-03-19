package org.web.webdav.common.enums;

import lombok.Getter;

@Getter
public enum WebdavMethod {
    HEAD("HEAD"),
    PROPFIND("PROPFIND"),
    PROPPATCH("PROPPATCH"),
    MKCOL("MKCOL"),
    MKCALENDAR("MKCALENDAR"),
    COPY("COPY"),
    MOVE("MOVE"),
    LOCK("LOCK"),
    UNLOCK("UNLOCK"),
    DELETE("DELETE"),
    GET("GET"),
    OPTIONS("OPTIONS"),
    POST("POST"),
    PUT("PUT"),
    TRACE("TRACE"),
    ACL("ACL"),
    CONNECT("CONNECT"),
    CANCELUPLOAD("CANCELUPLOAD"),
    REPORT("REPORT");
    
    public final String code;
    
    WebdavMethod(String method) {
        this.code = method;
    }
    
    public static WebdavMethod getMethod(String method) {
        for (WebdavMethod webdavMethod : WebdavMethod.values()) {
            if (webdavMethod.code.equals(method)) {
                return webdavMethod;
            }
        }
        return WebdavMethod.HEAD;
    }
}
