package org.web.neteasecloudmusic.security.exception;

import cn.hutool.core.convert.Convert;
import jakarta.servlet.http.HttpServletRequest;
import org.core.common.annotation.AnonymousAccess;
import org.core.common.constant.ExceptionPathConstant;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 此controller是用于异常转发
 */
@RestController
public class ExceptionController {
    @WebLog(LogNameConstant.REDIRECT_EXCEPTION)
    @AnonymousAccess
    @RequestMapping(value = ExceptionPathConstant.ERROR_PATH, method = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.PUT, RequestMethod.POST, RequestMethod.TRACE})
    public String errorException(HttpServletRequest request) throws Exception {
        throw Convert.convert(Exception.class, request.getAttribute(ExceptionPathConstant.ATTRIBUTE_EXCEPTION_IDENTIFIER));
    }
}
