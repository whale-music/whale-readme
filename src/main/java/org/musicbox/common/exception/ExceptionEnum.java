package org.musicbox.common.exception;

/**
 * @description: 异常处理枚举类
 * @author: DT
 * @date: 2021/4/19 21:41
 * @version: v1.0
 */
public enum ExceptionEnum implements BaseErrorInfoInterface {
    
    SUCCESS("200", "成功!"),
    
    
    // 数据操作错误定义
    // 3000 服务器错误
    // 4000 数据(文件)错误
    // 5000 业务错误
    DUPLICATE_USER_NAME_ERROR("5001", "用户名不能重复!"),
    USER_DOES_NOT_EXIST("5002", "用户名不存在!"),
    
    BODY_NOT_MATCH("4000", "请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH("4001", "请求的数字签名不匹配!"),
    NOT_FOUND("4004", "未找到该资源!"),
    INTERNAL_SERVER_ERROR("5000", "服务器内部错误!"),
    SERVER_BUSY("5003", "服务器正忙，请稍后再试!");
    
    /**
     * 错误码
     */
    private final String resultCode;
    
    /**
     * 错误描述
     */
    private final String resultMsg;
    
    ExceptionEnum(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
    
    @Override
    public String getResultCode() {
        return resultCode;
    }
    
    @Override
    public String getResultMsg() {
        return resultMsg;
    }
}
