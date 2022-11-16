package org.musicbox.exception;

import lombok.extern.slf4j.Slf4j;
import org.musicbox.common.result.R;
import org.musicbox.common.result.ResultCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
    
    @ExceptionHandler(DuplicateUserNameException.class)
    @ResponseBody
    public R exceptionHandler1(HttpServletRequest req, BaseException e) {
        log.error("用户名不能重复：{}", e.getErrorMsg());
        return R.error(e.getErrorCode(), e.getErrorMsg());
    }
    
    @ExceptionHandler(UserDoesNotExistException.class)
    @ResponseBody
    public R exceptionHandler2(HttpServletRequest req, BaseException e) {
        log.warn("用户不存在{}", e.getErrorMsg());
        return R.error(e.getErrorCode(), e.getErrorMsg());
    }
    
    @ExceptionHandler(CookieInvalidException.class)
    @ResponseBody
    public R exceptionHandler3(HttpServletRequest req, BaseException e) {
        log.warn("Cookie无效{}", e.getErrorMsg());
        return R.error(e.getErrorCode(), e.getErrorMsg());
    }
    
    @ExceptionHandler(SongListDoesNotExistException.class)
    @ResponseBody
    public R exceptionHandler4(HttpServletRequest req, BaseException e) {
        log.warn("歌单不存在{}", e.getErrorMsg());
        return R.error(e.getErrorCode(), e.getErrorMsg());
    }
    
    @ExceptionHandler(NoAuthorityException.class)
    @ResponseBody
    public R exceptionHandler5(HttpServletRequest req, BaseException e) {
        log.warn("无权限操作{}", e.getErrorMsg());
        return R.error(e.getErrorCode(), e.getErrorMsg());
    }
    
    @ExceptionHandler(SongNotExistException.class)
    @ResponseBody
    public R exceptionHandler6(HttpServletRequest req, BaseException e) {
        log.warn("歌曲不存在{}", e.getErrorMsg());
        return R.error(e.getErrorCode(), e.getErrorMsg());
    }
    
    @ExceptionHandler(SongExistException.class)
    @ResponseBody
    public R exceptionHandler7(HttpServletRequest req, BaseException e) {
        log.warn("歌曲已存在{}", e.getErrorMsg());
        return R.error(e.getErrorCode(), e.getErrorMsg());
    }
    
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
