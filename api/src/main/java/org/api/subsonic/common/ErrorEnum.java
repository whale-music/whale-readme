package org.api.subsonic.common;

public enum ErrorEnum {
    
    A_GENERIC_ERROR(0, "一般性错误"),
    REQUIRED_PARAMETER_MISSING(10, "缺少必需的参数"),
    CLIENT_MUST_UPGRADE(20, "REST协议版本不兼容。客户端必须升级"),
    SERVER_MUST_UPGRADE(30, "REST协议版本不兼容。服务器必须升级"),
    WRONG_USERNAME_OR_PASSWORD(40, "用户名或密码错误"),
    USER_IS_NOT_AUTHORIZED(50, "用户未被授权执行给定操作"),
    SUBSONIC_SERVER_IS_OVER(60, "Subsonic服务器的试用期已经结束。请升级到Subsonic Premium。请访问subsonic.org了解详情"),
    REQUESTED_DATA_WAS_NOT_FOUND(70, "找不到请求的数据");
    
    
    
    private final Integer code;
    private final String message;
    
    ErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getResultMsg() {
        return message;
    }
    
    public Error error() {
        return new Error(this.code, this.message);
    }
}
