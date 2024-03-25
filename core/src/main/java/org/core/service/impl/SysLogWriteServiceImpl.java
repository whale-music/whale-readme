package org.core.service.impl;

import org.core.common.weblog.model.WebLogRecord;
import org.core.mybatis.iservice.SysLogService;
import org.core.mybatis.pojo.SysLogPojo;
import org.core.service.SysLogWriteService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("sysLogWriteServiceImpl")
public class SysLogWriteServiceImpl implements SysLogWriteService {
    private final SysLogService sysLogoService;
    
    public SysLogWriteServiceImpl(SysLogService sysLogoService) {
        this.sysLogoService = sysLogoService;
    }
    
    /**
     * 写入controller请求日志记录, 该方法是异步的
     *
     * @param webLogRecord 日志信息
     */
    @Async
    @Override
    public void writeWebLogRecord(WebLogRecord webLogRecord) {
        SysLogPojo sysLogPojo = new SysLogPojo();
        sysLogPojo.setLogName(webLogRecord.getLogName());
        sysLogPojo.setExecutionTime(webLogRecord.getExecutionTime());
        sysLogPojo.setStartTime(webLogRecord.getStartTime());
        sysLogPojo.setEndTime(webLogRecord.getEndTime());
        sysLogPojo.setThreadId(webLogRecord.getThreadId());
        sysLogPojo.setThreadName(webLogRecord.getThreadName());
        sysLogPojo.setProcessId(webLogRecord.getProcessId());
        
        if (Objects.nonNull(webLogRecord.getMethodCallDetail())) {
            WebLogRecord.MethodCallDetail methodCallDetail = webLogRecord.getMethodCallDetail();
            sysLogPojo.setMethodParamsData(methodCallDetail.getMethodParamsData());
            sysLogPojo.setMethodParamsType(methodCallDetail.getMethodParamsType());
            sysLogPojo.setMethodResultParamType(methodCallDetail.getMethodResultParamType());
            sysLogPojo.setMethodName(methodCallDetail.getMethodName());
        }
        
        if (Objects.nonNull(webLogRecord.getWebRequest())) {
            WebLogRecord.WebRequest webRequest = webLogRecord.getWebRequest();
            sysLogPojo.setRequestMethod(webRequest.getRequestMethod());
            sysLogPojo.setFullPath(webRequest.getFullPath());
            sysLogPojo.setRemoteHost(webRequest.getRemoteHost());
            sysLogPojo.setMappingPath(webRequest.getMappingPath());
            if (Objects.nonNull(webRequest.getUserAgent())) {
                WebLogRecord.WebRequest.UserAgent userAgent = webRequest.getUserAgent();
                sysLogPojo.setUserAgentContent(userAgent.getUserAgentContent());
                sysLogPojo.setMobile(userAgent.isMobile());
                sysLogPojo.setBrowser(userAgent.getBrowser());
                sysLogPojo.setBrowserVersion(userAgent.getBrowserVersion());
                sysLogPojo.setPlatform(userAgent.getPlatform());
                sysLogPojo.setOs(userAgent.getOs());
                sysLogPojo.setOsVersion(userAgent.getOsVersion());
                sysLogPojo.setRenderingEngine(userAgent.getRenderingEngine());
                sysLogPojo.setRenderingEngineVersion(userAgent.getRenderingEngineVersion());
            }
        }
        
        if (Objects.nonNull(webLogRecord.getExceptionReport())) {
            WebLogRecord.ExceptionReport exceptionReport = webLogRecord.getExceptionReport();
            sysLogPojo.setExceptionMessage(exceptionReport.getExceptionMessage());
            sysLogPojo.setExceptionStack(exceptionReport.getExceptionStack());
            sysLogPojo.setExceptionLineNumber(exceptionReport.getExceptionLineNumber());
            sysLogPojo.setExceptionClassName(exceptionReport.getExceptionClassName());
        }
        
        sysLogoService.save(sysLogPojo);
    }
}
