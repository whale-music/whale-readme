package org.musicbox.exception;

import lombok.extern.slf4j.Slf4j;
import org.musicbox.common.result.R;
import org.musicbox.common.result.ResultCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 *
 * @author Sakura
 * @since 2022-10-22
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public R bizExceptionHandler(HttpServletRequest req, BaseException e) {
        log.error("发生业务异常！原因是：{}", e.getErrorMsg());
        return R.error(e.getErrorCode(), e.getErrorMsg());
    }
    
    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    @ResponseBody
    public R exceptionHandler8(HttpServletRequest req, NullPointerException e) {
        log.error("上传文件太大:", e);
        return R.error(e.getMessage());
    }
    
    
    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest req, NullPointerException e) {
        log.error("发生空指针异常！原因是:", e);
        return R.error(ResultCode.NULLPOINTEREXCEPTION);
    }
    
    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("未知异常！原因是:", e);
        return R.error(ResultCode.INTERNAL_SERVER_ERROR);
    }
}
