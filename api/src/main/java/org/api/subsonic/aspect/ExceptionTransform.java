package org.api.subsonic.aspect;

import org.api.subsonic.common.ErrorEnum;
import org.core.common.exception.BaseErrorInfoInterface;
import org.core.common.result.ResultCode;

import java.util.Map;

public class ExceptionTransform {
    private static final Map<BaseErrorInfoInterface, ErrorEnum> errorsMap = Map.of(
            // 登录错误, 用户
            ResultCode.USER_NOT_EXIST, ErrorEnum.WRONG_USERNAME_OR_PASSWORD,
            // 登录错误, 密码
            ResultCode.PASSWORD_ERROR, ErrorEnum.WRONG_USERNAME_OR_PASSWORD,
            // 音源没有找到
            ResultCode.RESOURCE_DATA_NOT_EXISTED, ErrorEnum.REQUESTED_DATA_WAS_NOT_FOUND
    );
    
    private ExceptionTransform() {
    }
    
    public static ErrorEnum get(BaseErrorInfoInterface code) {
        return errorsMap.get(code);
    }
    
}
