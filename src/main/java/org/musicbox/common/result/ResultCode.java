package org.musicbox.common.result;

import org.musicbox.common.exception.BaseErrorInfoInterface;

/**
 * &#064;description:  异常处理枚举类
 */
public enum ResultCode implements BaseErrorInfoInterface {
    /* 默认成功状态码 */
    SUCCESS("200", "操作成功"),
    
    
    /* 默认失败状态码 */
    ERROR("100", "操作失败，未知指定错误信息"),
    
    
    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID("10001", "参数无效"),
    PARAM_IS_BLANK("10002", "参数为空"),
    PARAM_TYPE_BIND_ERROR("10003", "参数类型错误"),
    PARAM_NOT_COMPLETE("10004", "参数缺失"),
    TOKEN_INVALID("10004", "Token 无效"),
    COOKIE_INVALID("10005", "Cookie 无效"),
    // 文件名无效
    FILENAME_INVALID("10006", "文件名无效"),
    
    
    /* 用户错误：20001-29999*/
    USER_NOT_LOGIN("20001", "用户未登录"),
    
    USER_LOGIN_ERROR("20002", "账号不存在或密码错误"),
    
    USER_ACCOUNT_FORBIDDEN("20003", "账号已被禁用"),
    
    USER_HAS_EXISTED("20005", "用户已存在"),
    
    USER_NOT_EXIST("20006", "用户名不存在!"),
    
    DUPLICATE_USER_NAME_ERROR("20007", "用户名不能重复!"),
    
    SONG_LIST_DOES_NOT_EXIST("20008", "歌单不存在!"),
    
    SONG_NOT_EXIST("20009", "歌曲不存在!"),
    
    SONG_EXIST("20010", "歌曲已存在!"),
    
    
    /* 业务错误：30001-39999 */
    SPECIFIED_QUESTIONED_USER_NOT_EXIST("30001", "业务逻辑出现问题"),
    
    NULLPOINTEREXCEPTION("30002", "空指针错误"),
    
    
    /* 系统错误：40001-49999 */
    SYSTEM_INNER_ERROR("40001", "系统内部错误，请稍后重试"),
    
    INTERNAL_SERVER_ERROR("40002", "未知异常"),
    
    /* 数据错误：50001-599999 */
    DATA_NONE("50001", "数据未找到"),
    
    DATA_WRONG("50002", "数据错误"),
    
    DATA_EXISTED("50003", "数据已存在"),
    
    /* 接口错误：60001-69999 */
    INTERFACE_INNER_INVOKE_ERROR("60001", "内部系统接口调用异常"),
    
    INTERFACE_OUTTER_INVOKE_ERROR("60002", "外部系统接口调用异常"),
    
    INTERFACE_FORBID_VISIT("60003", "该接口禁止访问"),
    
    INTERFACE_ADDRESS_INVALID("60004", "接口地址无效"),
    
    INTERFACE_REQUEST_TIMEOUT("60005", "接口请求超时"),
    
    
    /* 权限错误：70001-79999 */
    PERMISSION_NO_ACCESS("70001", "无访问权限");
    
    /**
     * 错误码
     */
    private final String code;
    
    /**
     * 错误描述
     */
    private final String resultMsg;
    
    ResultCode(String code, String resultMsg) {
        this.code = code;
        this.resultMsg = resultMsg;
    }
    
    @Override
    public String getCode() {
        return code;
    }
    
    @Override
    public String getResultMsg() {
        return resultMsg;
    }
}
