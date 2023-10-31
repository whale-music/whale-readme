package org.web.neteasecloudmusic.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.common.exception.BaseErrorInfoInterface;
import org.common.exception.BaseException;
import org.common.properties.DebugConfig;
import org.common.result.R;
import org.common.result.ResultCode;
import org.core.common.result.NeteaseResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 全局异常处理
 *
 * @author Sakura
 * @since 2022-10-22
 */
@ControllerAdvice
@Slf4j
public class NeteaseGlobalExceptionHandler {
    public static final String THROW_STR = "Throwable: ";
    
    private static final Map<String, BaseErrorInfoInterface> convertErrorCode = new HashMap<>();
    
    static {
        convertErrorCode.put(ResultCode.USER_NOT_LOGIN.getCode(), new BaseException("301", "需要登录"));
    }
    
    // 转换core中的异常变成Netease中的异常代码
    private BaseErrorInfoInterface convert(Exception e) {
        if (e instanceof BaseException baseException) {
            BaseErrorInfoInterface errorInfoInterface = convertErrorCode.get(baseException.getCode());
            if (Objects.isNull(errorInfoInterface)) {
                return new BaseException("400", "参数错误");
            } else {
                return new BaseException(errorInfoInterface.getCode(), errorInfoInterface.getResultMsg());
            }
        } else {
            return new BaseException("400", "参数错误");
        }
    }
    
    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public ResponseEntity<NeteaseResult> bizExceptionHandler(BaseException e) {
        log.error("发生业务异常！原因是：{}", e.getResultMsg());
        Optional.of(DebugConfig.getDebug()).ifPresent(aBoolean -> log.error(THROW_STR, e));
        BaseErrorInfoInterface convert = convert(e);
        if (StringUtils.equals(convert.getCode(), "301")) {
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).body(new NeteaseResult().error(convert));
        } else {
            return ResponseEntity.ok(new NeteaseResult().error(convert));
        }
    }
    
    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    @ResponseBody
    public ResponseEntity<NeteaseResult> exceptionHandler8(MaxUploadSizeExceededException e) {
        log.error("上传文件太大:", e);
        Optional.of(DebugConfig.getDebug()).ifPresent(aBoolean -> log.error(THROW_STR, e));
        return ResponseEntity.ok(new NeteaseResult().error(convert(e)));
    }
    
    
    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResponseEntity<NeteaseResult> exceptionHandler(NullPointerException e) {
        log.error("发生空指针异常！原因是:", e);
        Optional.of(DebugConfig.getDebug()).ifPresent(aBoolean -> log.error(THROW_STR, e));
        return ResponseEntity.ok(new NeteaseResult().error(convert(e)));
    }
    
    /**
     * SQL 异常
     */
    @ExceptionHandler(value = BadSqlGrammarException.class)
    @ResponseBody
    public ResponseEntity<NeteaseResult> exceptionHandler(BadSqlGrammarException e) {
        log.error("SQL运行错误原因是:", e);
        Optional.of(DebugConfig.getDebug()).ifPresent(aBoolean -> log.error(THROW_STR, e));
        return ResponseEntity.ok(new NeteaseResult().error(convert(e)));
    }
    
    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<NeteaseResult> exceptionHandler(Exception e) {
        log.error("未知异常！原因是:", e);
        Optional.of(DebugConfig.getDebug()).ifPresent(aBoolean -> log.error(THROW_STR, e));
        R error = R.error(ResultCode.INTERNAL_SERVER_ERROR);
        error.setMessage(error.getMessage() + ": " + e);
        return ResponseEntity.ok(new NeteaseResult().error(convert(e)));
    }
}
