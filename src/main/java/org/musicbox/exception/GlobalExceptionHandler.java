package org.musicbox.exception;

import lombok.extern.slf4j.Slf4j;
import org.musicbox.common.result.R;
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
    
    /**
     * 处理自定义的业务异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(DuplicateUserNameException.class)
    @ResponseBody
    public R userRepetitionExceptionHandler(HttpServletRequest req, BaseException e) {
        log.error("用户名不能重复：{}", e.getErrorMsg());
        return R.error(e.getErrorCode(), e.getErrorMsg());
    }
    
    /**
     * 用户不存在
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(UserDoesNotExistException.class)
    @ResponseBody
    public R userDoesNotExistExceptionHandler(HttpServletRequest req, BaseException e) {
        log.warn("用户不存在{}", e.getErrorMsg());
        return R.error(e.getErrorCode(), e.getErrorMsg());
    }
    
    
    // /**
    //  * 处理自定义的业务异常
    //  * @param req
    //  * @param e
    //  * @return
    //  */
    // @ExceptionHandler(value = BaseException.class)
    // @ResponseBody
    // public R bizExceptionHandler(HttpServletRequest req, BaseException e){
    //     log.error("发生业务异常！原因是：{}",e.getErrorMsg());
    //     return R.error(e.getErrorCode(),e.getErrorMsg());
    // }
    
    // /**
    //  * 处理空指针的异常
    //  * @param req
    //  * @param e
    //  * @return
    //  */
    // @ExceptionHandler(value =NullPointerException.class)
    // @ResponseBody
    // public R exceptionHandler(HttpServletRequest req, NullPointerException e){
    //     log.error("发生空指针异常！原因是:",e);
    //     return R.error(ResultCode.BODY_NOT_MATCH);
    // }
    
    // /**
    //  * 处理其他异常
    //  * @param req
    //  * @param e
    //  * @return
    //  */
    // @ExceptionHandler(value =Exception.class)
    // @ResponseBody
    // public R exceptionHandler(HttpServletRequest req, Exception e){
    //     log.error("未知异常！原因是:",e);
    //     return R.error(ResultCode.INTERNAL_SERVER_ERROR);
    // }
}
