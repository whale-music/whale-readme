package org.web.nmusic.security.handle;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.core.common.constant.ExceptionPathConstant;
import org.core.common.exception.BaseException;
import org.core.common.properties.DebugConfig;
import org.core.common.result.ResultCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

/**
 * 匿名用户访问无权限资源时的异常
 */
@Component
@Slf4j
public class AnonymousAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    
    @Serial
    private static final long serialVersionUID = 897071117077606L;
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("visit uri {}", request.getRequestURI());
        Optional.of(DebugConfig.getDebug()).ifPresent(aBoolean -> log.error(authException.getMessage(), authException.fillInStackTrace()));
        request.setAttribute(ExceptionPathConstant.ATTRIBUTE_EXCEPTION_IDENTIFIER, new BaseException(ResultCode.USER_NOT_LOGIN));
        request.getRequestDispatcher(ExceptionPathConstant.ERROR_PATH).forward(request, response);
    }
}
