package org.core.common.weblog.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.http.Header;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.core.common.exception.BaseException;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.model.WebLogRecord;
import org.core.service.SysLogWriteService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@Aspect
@Slf4j
public class WeblogAspect {
    
    
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final SysLogWriteService sysLogWriteService;
    
    public WeblogAspect(SysLogWriteService sysLogWriteService) {
        this.sysLogWriteService = sysLogWriteService;
    }
    
    /**
     * 获取当前程序的进程ID
     *
     * @return pid
     */
    public static Long getPid() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName(); // format: "pid@hostname"
        try {
            return Long.valueOf(name.substring(0, name.indexOf('@')));
        } catch (Exception e) {
            return -1L;
        }
    }
    
    private static WebLogRecord.ExceptionReport getExceptionReport(Throwable e) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             PrintStream s = new PrintStream(byteArrayOutputStream, true, StandardCharsets.UTF_8)
        ) {
            WebLogRecord.ExceptionReport exceptionReport = new WebLogRecord.ExceptionReport();
            e.printStackTrace(s);
            exceptionReport.setExceptionStack(byteArrayOutputStream.toString(StandardCharsets.UTF_8));
            exceptionReport.setExceptionMessage(e.getMessage());
            // 获取方法行号，计划前端查看报错信息后自动跳转到git仓库。链接后添加报错行号，自动高亮
            StackTraceElement[] stackTrace = e.getStackTrace();
            StackTraceElement stackTraceElement = stackTrace[0];
            exceptionReport.setExceptionLineNumber(stackTraceElement.getLineNumber());
            exceptionReport.setExceptionClassName(stackTraceElement.getClassName());
            return exceptionReport;
        } catch (Exception ex) {
            throw new BaseException(ex.getMessage());
        }
    }
    
    /**
     * 获取前端请求信息
     *
     * @return web log
     */
    @NotNull
    private static WebLogRecord.WebRequest getWebRequest() {
        WebLogRecord.WebRequest webRequest = new WebLogRecord.WebRequest();
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes requestAttributes) {
            HttpServletRequest request = requestAttributes.getRequest();
            webRequest.setRequestMethod(request.getMethod());
            webRequest.setFullPath(String.valueOf(request.getRequestURL()));
            webRequest.setRemoteHost(request.getRemoteHost());
            webRequest.setMappingPath(request.getRequestURI());
            String userAgent = request.getHeader(Header.USER_AGENT.toString());
            WebLogRecord.WebRequest.UserAgent webUserAgent = new WebLogRecord.WebRequest.UserAgent();
            UserAgent parse = UserAgentUtil.parse(userAgent);
            webUserAgent.setUserAgentContent(userAgent);
            webUserAgent.setMobile(Boolean.TRUE.equals(parse.getBrowser().isMobile()));
            webUserAgent.setBrowser(parse.getBrowser().getName());
            webUserAgent.setBrowserVersion(parse.getVersion());
            webUserAgent.setPlatform(parse.getPlatform().getName());
            webUserAgent.setOs(parse.getOs().getName());
            webUserAgent.setOsVersion(parse.getOsVersion());
            webUserAgent.setRenderingEngine(parse.getEngine().getName());
            webUserAgent.setRenderingEngineVersion(parse.getEngineVersion());
            webRequest.setUserAgent(webUserAgent);
        }
        return webRequest;
    }
    
    @Pointcut("@annotation(org.core.common.weblog.annotation.WebLog)")
    public void pointCut() {
    }
    
    @Around(value = "pointCut()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        WebLogRecord webLogRecord = new WebLogRecord();
        Object proceed = null;
        // 执行时间
        StopWatch sw = new StopWatch();
        // 开始时间
        webLogRecord.setStartTime(LocalDateTime.now());
        try {
            sw.start();
            proceed = proceedingJoinPoint.proceed();
            return proceed;
        } catch (Throwable e) {
            webLogRecord.setExceptionReport(getExceptionReport(e));
            throw e;
        } finally {
            sw.stop();
            // 结束时间
            webLogRecord.setEndTime(LocalDateTime.now());
            // 方法执行时间
            webLogRecord.setExecutionTime((int) sw.getLastTaskTimeMillis());
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = signature.getMethod();
            WebLog webLog = method.getAnnotation(WebLog.class);
            webLogRecord.setLogName(webLog.value());
            // 记录前端请求
            WebLogRecord.WebRequest webRequest = getWebRequest();
            webLogRecord.setWebRequest(webRequest);
            log.debug("uri: {}", webRequest.getMappingPath());
            // 方法执行信息
            webLogRecord.setMethodCallDetail(getMethodCallDetail(proceedingJoinPoint, proceed));
            long threadId = Thread.currentThread().threadId();
            webLogRecord.setThreadId(threadId);
            String threadName = Thread.currentThread().getName();
            webLogRecord.setThreadName(threadName);
            webLogRecord.setProcessId(getPid());
            
            sysLogWriteService.writeWebLogRecord(webLogRecord);
        }
    }
    
    private WebLogRecord.MethodCallDetail getMethodCallDetail(@NotNull ProceedingJoinPoint proceedingJoinPoint, Object proceed) {
        WebLogRecord.MethodCallDetail methodCallDetail = new WebLogRecord.MethodCallDetail();
        Signature signature = proceedingJoinPoint.getSignature();
        try {
            methodCallDetail.setMethodParamsData(OBJECT_MAPPER.writeValueAsString(proceedingJoinPoint.getArgs()));
        } catch (JsonProcessingException e) {
            methodCallDetail.setMethodParamsData("Json parsing error");
        }
        List<Object> list = Arrays.stream(proceedingJoinPoint.getArgs()).filter(Objects::nonNull).toList();
        if (CollUtil.isNotEmpty(list)) {
            List<String> paramNames = list.stream()
                                          .map(Object::getClass)
                                          .map(Class::getName)
                                          .toList();
            methodCallDetail.setMethodParamsType(CollUtil.join(paramNames, ","));
        }
        methodCallDetail.setMethodName(signature.getDeclaringTypeName() + '#' + signature.getName());
        methodCallDetail.setMethodResultParamType(proceed == null ? null : proceed.getClass().getName());
        return methodCallDetail;
    }
}
