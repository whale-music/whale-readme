package org.web.admin.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.core.common.exception.BaseException;
import org.core.common.result.R;
import org.core.common.result.ResultCode;
import org.core.config.DebugConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Optional;

/**
 * 全局异常处理
 *
 * @author Sakura
 * @since 2022-10-22
 */
@ControllerAdvice
@Slf4j
public class AdminGlobalExceptionHandler {
    public static final String THROW_STR = "Throwable: ";
    
    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public ResponseEntity<R> bizExceptionHandler(BaseException e) {
        log.error("发生业务异常！原因是：{}", e.getResultMsg());
        Optional.of(DebugConfig.getDebug()).ifPresent(aBoolean -> log.error(THROW_STR, e));
        return ResponseEntity.ok(R.error(e.getCode(), e.getResultMsg()));
    }
    
    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    @ResponseBody
    public ResponseEntity<R> exceptionHandler8(NullPointerException e) {
        log.error("上传文件太大:", e);
        Optional.of(DebugConfig.getDebug()).ifPresent(aBoolean -> log.error(THROW_STR, e));
        return ResponseEntity.ok(R.error(e.getMessage()));
    }
    
    
    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResponseEntity<R> exceptionHandler(NullPointerException e) {
        log.error("发生空指针异常！原因是:", e);
        Optional.of(DebugConfig.getDebug()).ifPresent(aBoolean -> log.error(THROW_STR, e));
        return ResponseEntity.ok(R.error(ResultCode.NULL_POINTER_EXCEPTION));
    }
    
    /**
     * SQL 异常
     */
    @ExceptionHandler(value = BadSqlGrammarException.class)
    @ResponseBody
    public ResponseEntity<R> exceptionHandler(BadSqlGrammarException e) {
        log.error("SQL运行错误原因是:", e);
        Optional.of(DebugConfig.getDebug()).ifPresent(aBoolean -> log.error(THROW_STR, e));
        return ResponseEntity.ok(R.error(ResultCode.SQL_RUN_ERROR));
    }
    
    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<R> exceptionHandler(Exception e) {
        log.error("未知异常！原因是:", e);
        Optional.of(DebugConfig.getDebug()).ifPresent(aBoolean -> log.error(THROW_STR, e));
        R error = R.error(ResultCode.INTERNAL_SERVER_ERROR);
        error.setMessage(error.getMessage() + ": " + e);
        return ResponseEntity.ok(error);
    }
}
