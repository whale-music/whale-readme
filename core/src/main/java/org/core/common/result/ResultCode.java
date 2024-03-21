package org.core.common.result;

import org.core.common.exception.BaseErrorInfoInterface;
import org.core.utils.i18n.I18nUtil;

/**
 * 异常处理枚举类
 */
public enum ResultCode implements BaseErrorInfoInterface {
    /* 默认成功状态码 */
    // 操作成功
    SUCCESS("200", "error.messages.success"),
    
    
    /* 默认失败状态码 */
    // 操作失败，未知指定错误信息
    ERROR("100", "error.messages.error"),
    // 二维码不存在或已过期
    QR_ERROR("800", "error.messages.qr_error"),
    
    /* 参数错误：10001-19999 */
    // 参数无效
    PARAM_IS_INVALID("10001", "error.messages.param_is_invalid"),
    // 参数为空
    PARAM_IS_BLANK("10002", "error.messages.param_is_blank"),
    // 参数类型错误
    PARAM_TYPE_BIND_ERROR("10003", "error.messages.param_type_bind_error"),
    // 参数缺失
    PARAM_NOT_COMPLETE("10004", "error.messages.param_not_complete"),
    // Cookie 无效
    COOKIE_INVALID("10005", "error.messages.cookie_invalid"),
    // 文件名无效
    FILENAME_INVALID("10006", "error.messages.filename_invalid"),
    // 存储名称无效
    SAVE_NAME_INVALID("10007", "error.messages.save_name_invalid"),
    // 插件不存在
    PLUGIN_NO_EXISTED("10008", "error.messages.plugin_no_existed"),
    // 文件名不存在
    FILENAME_NO_EXIST("10009", "error.messages.filename_no_exist"),
    // 歌单不存在
    PLAY_LIST_NO_EXIST("10010", "error.messages.play_list_no_exist"),
    // 歌单中已存在歌曲
    PLAY_LIST_MUSIC_EXIST("10011", "error.messages.play_list_music_exist"),
    // 专辑不存在
    ALBUM_NO_EXIST_ERROR("10012", "error.messages.album_no_exist_error"),
    // 插件类型不存在
    PLUGIN_NO_EXIST_EXISTED("10013", "error.messages.plugin_no_exist_existed"),
    // 歌词不存在
    LYRIC_NO_EXIST_EXISTED("10014", "error.messages.lyric_no_exist_existed"),
    // 定时任务不存在
    SCHEDULED_NO_EXIST_EXISTED("10015", "error.messages.scheduled_no_exist_existed"),
    // Cron表达式错误, 请检查重试
    CRON_ERROR("10016", "error.messages.cron_error"),
    // 歌单已收藏
    PLAY_LIST_LIKE("10017", "error.messages.play_list_like"),
    // 存储路径不存在
    STORAGE_PATH_DOES_NOT_EXIST("10018", "error.messages.storage_path_does_not_exist"),
    // 存储文件不存在
    STORAGE_FILE_DOES_NOT_EXIST_ERROR("10019", "error.messages.storage_file_does_not_exist"),
    // Token已过期
    TOKEN_EXPIRED_ERROR("10020", "error.messages.token_expired"),
    
    
    /* 用户错误：20001-29999*/
    // 用户未登录
    USER_NOT_LOGIN("20001", "error.messages.user_not_login"),
    // 账号不存在或密码错误
    ACCOUNT_DOES_NOT_EXIST_OR_WRONG_PASSWORD("20002", "error.messages.account_does_not_exist_or_wrong_password"),
    // 账号已被禁用
    USER_ACCOUNT_FORBIDDEN("20003", "error.messages.user_account_forbidden"),
    // 用户已存在
    USER_HAS_EXISTED("20005", "error.messages.user_has_existed"),
    // 用户不存在
    USER_NOT_EXIST("20006", "error.messages.user_not_exist"),
    // 用户名不能重复
    DUPLICATE_USER_NAME_ERROR("20007", "error.messages.duplicate_user_name_error"),
    // 歌单不存在
    SONG_LIST_DOES_NOT_EXIST("20008", "error.messages.song_list_does_not_exist"),
    // 歌曲不存在
    SONG_NOT_EXIST("20009", "error.messages.song_not_exist"),
    // 数据库中歌曲已存在
    SONG_EXIST("20010", "error.messages.song_exist"),
    // 歌曲已上传
    SONG_UPLOADED("20011", "error.messages.song_uploaded"),
    // 数据库中有多个相似歌曲请手动添加
    MULTIPLE_SONGS("20012", "error.messages.multiple_songs"),
    // 专辑已存在
    ALBUM_NOT_EXIST("20013", "error.messages.album_not_exist"),
    // 专辑错误
    ALBUM_ERROR("20014", "error.messages.album_error"),
    // 密码错误
    PASSWORD_ERROR("20015", "error.messages.password_error"),
    // 歌单包括音乐
    COLLECT_MUSIC_ERROR("20016", "error.messages.collect_music_error"),
    // 专辑有关联音乐
    ALBUM_MUSIC_EXIST_ERROR("20017", "error.messages.album_music_exist_error"),
    // 艺术家不存在
    ARTIST_NO_EXIST_ERROR("20018", "error.messages.artist_no_exist_error"),
    // 上传歌曲文件存在于其他歌曲中
    UPLOAD_MUSIC_ID_NOT_MATCH("20019", "error.messages.upload_music_id_not_match"),
    // 管理员用户不能修改账户状态
    ADMIN_USER_NOT_EDIT_STATUS("20020", "error.messages.admin_user_not_edit_status"),
    // 不能删除管理员账户
    ADMIN_CANNOT_DELETE("20021", "error.messages.admin_cannot_delete"),
    // 文件大小不能为零
    FILE_SIZE_CANNOT_BE_ZERO("20022", "error.messages.file_size_cannot_be_zero"),
    // 子账户已存在
    SUB_ACCOUNT_EXISTS("20023", "error.messages.sub_account_exists"),
    
    
    /* 业务错误：30001-39999 */
    // 业务逻辑出现问题
    SPECIFIED_QUESTIONED_USER_NOT_EXIST("30001", "error.messages.specified_questioned_user_not_exist"),
    // 空指针错误
    NULL_POINTER_EXCEPTION("30002", "error.messages.null_pointer_exception"),
    // 插件删除任务错误
    PLUGIN_DELETE_TASK_ERROR("30003", "error.messages.plugin_delete_task_error"),
    // 插件删除错误
    PLUGIN_DELETE_ERROR("30004", "error.messages.plugin_delete_error"),
    // 不能使用批量删除正在运行的插件,如需要强行删除手动点击单个插件删除按钮
    PLUGIN_CANNOT_DELETE_RUNNING("30005", "error.messages.plugin_cannot_delete_running"),
    // 数据库运行错误
    SQL_RUN_ERROR("30006", "error.messages.sql_run_error"),
    // 关联图片错误
    LINK_PIC_ERROR("30007", "error.messages.link_pic_error"),
    // 关联音频文件错误
    LINK_AUDIO_ERROR("30007", "error.messages.link_audio_error"),
    
    
    /* 系统错误：40001-49999 */
    // 系统内部错误，请稍后重试
    SYSTEM_INNER_ERROR("40001", "error.messages.system_inner_error"),
    // 未知异常
    INTERNAL_SERVER_ERROR("40002", "error.messages.internal_server_error"),
    // 本地文件
    LOCAL_FILE_READ_ERROR("40003", "error.messages.local_file_read_error"),
    
    /* 数据错误：50001-599999 */
    // 数据未找到
    DATA_NONE_FOUND("50001", "error.messages.data_none_found"),
    // 数据错误
    DATA_WRONG("50002", "error.messages.data_wrong"),
    // 数据已存在
    DATA_EXISTED("50003", "error.messages.data_existed"),
    // 保存错误!!请检查日志
    SAVE_FAIL("50004", "error.messages.save_fail"),
    // 插件代码数据错误，检查代码，或直接删除重新创建
    PLUGIN_CODE("50005", "error.messages.plugin_code"),
    // 存储错误
    OSS_ERROR("50006", "error.messages.oss_error"),
    // OSS登录错误
    OSS_LOGIN_ERROR("50007", "error.messages.oss_login_error"),
    // OSS上传错误
    OSS_UPLOAD_ERROR("50008", "error.messages.oss_upload_error"),
    // 下载错误
    DOWNLOAD_ERROR("50009", "error.messages.download_error"),
    // 序列化错误
    SERIALIZATION_ERROR("50009", "error.messages.serialization_error"),
    // 上传音乐MD5重复
    OSS_MD5_REPEAT("50011", "error.messages.oss_md5_repeat"),
    // 上传错误
    UPLOAD_ERROR("50012", "error.messages.upload_error"),
    // 只允许用户同时拥有一个歌单
    USER_LOVE_ERROR("50013", "error.messages.user_love_error"),
    // 删除失败
    OSS_REMOVE_ERROR("50014", "error.messages.oss_remove_error"),
    // 访问存储错误，请检查存储
    OSS_CONNECT_ERROR("50015", "error.messages.oss_connect_error"),
    // 下载封面错误
    IMG_DOWNLOAD_ERROR("50016", "error.messages.img_download_error"),
    // 音源已存在，请不要添加相同歌曲
    RESOURCE_DATA_EXISTED("50017", "error.messages.resource_data_existed"),
    SAVE_MUSIC_META_DATA_WRITE_ERROR("50018", "error.messages.save_music_meta_data_write_error"),
    OSS_ACCESS_ERROR("50019", "error.messages.oss_access_error"),
    // OSS 数据错误
    OSS_DATA_ERROR("50019", "error.messages.oss_data_error"),
    // OSS 数据不存在
    OSS_DATA_DOES_NOT_EXIST("50020", "error.messages.oss_data_does_not_exist"),
    // 请先同步数据
    PLEASE_SYNCHRONIZE_THE_DATA_FIRST("50021", "error.messages.please_synchronize_the_data_first"),
    // 音源不存在
    RESOURCE_DATA_NOT_EXISTED("50022", "error.messages.resource_data_not_existed"),
    
    
    /* 接口错误：60001-69999 */
    // 内部系统接口调用异常
    INTERFACE_INNER_INVOKE_ERROR("60001", "error.messages.interface_inner_invoke_error"),
    // 外部系统接口调用异常
    INTERFACE_OUTER_INVOKE_ERROR("60002", "error.messages.interface_outer_invoke_error"),
    // 该接口禁止访问
    INTERFACE_FORBID_VISIT("60003", "error.messages.interface_forbid_visit"),
    // 接口地址无效
    INTERFACE_ADDRESS_INVALID("60004", "error.messages.interface_address_invalid"),
    // 接口请求超时
    INTERFACE_REQUEST_TIMEOUT("60005", "error.messages.interface_request_timeout"),
    
    
    /* 权限错误：70001-79999 */
    // 无访问权限
    PERMISSION_NO_ACCESS("70001", "error.messages.permission_no_access"),
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
        this.resultMsg = I18nUtil.getMsg(resultMsg);
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
