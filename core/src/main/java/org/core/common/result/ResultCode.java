package org.core.common.result;

import org.core.common.exception.BaseErrorInfoInterface;

/**
 * &#064;description:  异常处理枚举类
 */
public enum ResultCode implements BaseErrorInfoInterface {
    /* 默认成功状态码 */
    SUCCESS("200", "操作成功"),
    
    
    /* 默认失败状态码 */
    ERROR("100", "操作失败，未知指定错误信息"),
    QR_ERROR("800", "二维码不存在或已过期"),
    
    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID("10001", "参数无效"),
    PARAM_IS_BLANK("10002", "参数为空"),
    PARAM_TYPE_BIND_ERROR("10003", "参数类型错误"),
    PARAM_NOT_COMPLETE("10004", "参数缺失"),
    COOKIE_INVALID("10005", "Cookie 无效"),
    FILENAME_INVALID("10006", "文件名无效"),
    SAVE_NAME_INVALID("10007", "存储名称无效"),
    PLUGIN_EXISTED("10008", "插件不存在"),
    FILENAME_EXIST("10009", "文件名不存在"),
    PLAT_LIST_EXIST("10010", "歌单不存在"),
    PLAT_LIST_MUSIC_EXIST("10011", "歌单中已存在歌曲"),
    ALBUM_NO_EXIST_ERROR("10012", "专辑不存在"),
    PLUGIN_NO_EXIST_EXISTED("10013", "插件类型不存在"),
    LYRIC_NO_EXIST_EXISTED("10014", "插件类型不存在"),
    SCHEDULED_NO_EXIST_EXISTED("10015", "定时任务不存在"),
    CRON_ERROR("10016", "Cron表达式错误, 请检查重试"),
    PLAT_LIST_LIKE("10017", "歌单已收藏"),
    
    
    /* 用户错误：20001-29999*/
    USER_NOT_LOGIN("20001", "用户未登录"),
    USER_LOGIN_ERROR("20002", "账号不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN("20003", "账号已被禁用"),
    USER_HAS_EXISTED("20005", "用户已存在"),
    USER_NOT_EXIST("20006", "用户不存在!"),
    DUPLICATE_USER_NAME_ERROR("20007", "用户名不能重复!"),
    SONG_LIST_DOES_NOT_EXIST("20008", "歌单不存在!"),
    SONG_NOT_EXIST("20009", "歌曲不存在!"),
    SONG_EXIST("20010", "数据库中歌曲已存在!"),
    SONG_UPLOADED("20011", "歌曲已上传!"),
    MULTIPLE_SONGS("20012", "数据库中有多个相似歌曲请手动添加!"),
    ALBUM_NOT_EXIST("20013", "专辑已存在!"),
    ALBUM_ERROR("20014", "专辑错误!"),
    PASSWORD_ERROR("20015", "密码错误!"),
    COLLECT_MUSIC_ERROR("20016", "歌单包括音乐"),
    ALBUM_MUSIC_EXIST_ERROR("20017", "专辑有关联音乐!"),
    ARTIST_NO_EXIST_ERROR("20018", "艺术家不存在"),
    UPLOAD_MUSIC_ID_NOT_MATCH("20019", "上传歌曲文件存在于其他歌曲中"),
    ADMIN_USER_NOT_EDIT_STATUS("20020", "管理员用户不能修改账户状态"),
    ADMIN_CANNOT_DELETE("20021", "不能删除管理员账户"),
    
    
    /* 业务错误：30001-39999 */
    SPECIFIED_QUESTIONED_USER_NOT_EXIST("30001", "业务逻辑出现问题"),
    NULL_POINTER_EXCEPTION("30002", "空指针错误"),
    PLUGIN_DELETE_TASK_ERROR("30003", "插件删除任务错误"),
    PLUGIN_DELETE_ERROR("30004", "插件删除错误"),
    PLUGIN_CANNOT_DELETE_RUNNING("30005", "不能使用批量删除正在运行的插件,如需要强行删除手动点击单个插件删除按钮"),
    SQL_RUN_ERROR("30006", "数据库运行错误"),
    
    
    /* 系统错误：40001-49999 */
    SYSTEM_INNER_ERROR("40001", "系统内部错误，请稍后重试"),
    INTERNAL_SERVER_ERROR("40002", "未知异常"),
    LOCAL_FILE("40003", "本地文件"),
    
    /* 数据错误：50001-599999 */
    DATA_NONE("50001", "数据未找到"),
    
    DATA_WRONG("50002", "数据错误"),
    
    DATA_EXISTED("50003", "数据已存在"),
    SAVE_FAIL("50004", "保存错误!!请检查日志"),
    PLUGIN_CODE("50005", "插件代码数据错误，检查代码，或直接删除重新创建"),
    OSS_ERROR("50006", "存储错误"),
    OSS_LOGIN_ERROR("50007", "OSS登录错误"),
    OSS_UPLOAD_ERROR("50008", "OSS上传错误"),
    DOWNLOAD_ERROR("50009", "下载错误"),
    SERIALIZATION_ERROR("50009", "序列化错误"),
    OSS_MD5_REPEAT("50011", "上传音乐MD5重复"),
    
    UPLOAD_ERROR("50012", "上传错误"),
    USER_LOVE_ERROR("50013", "只允许用户同时拥有一个歌单"),
    OSS_REMOVE_ERROR("50014", "删除失败"),
    OSS_CONNECT_ERROR("50015", "访问存储错误，请检查存储"),
    IMG_DOWNLOAD_ERROR("50016", "下载封面错误"),
    RESOURCE_DATA_EXISTED("50017", "音源已存在，请不要添加相同歌曲"),
    
    
    /* 接口错误：60001-69999 */
    INTERFACE_INNER_INVOKE_ERROR("60001", "内部系统接口调用异常"),
    
    INTERFACE_OUTTER_INVOKE_ERROR("60002", "外部系统接口调用异常"),
    
    INTERFACE_FORBID_VISIT("60003", "该接口禁止访问"),
    
    INTERFACE_ADDRESS_INVALID("60004", "接口地址无效"),
    
    INTERFACE_REQUEST_TIMEOUT("60005", "接口请求超时"),
    
    
    /* 权限错误：70001-79999 */
    PERMISSION_NO_ACCESS("70001", "无访问权限"),
    ;
    
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
