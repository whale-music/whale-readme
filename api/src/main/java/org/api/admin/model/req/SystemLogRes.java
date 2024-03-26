package org.api.admin.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemLogRes {
    @Schema(name = "日志ID")
    private Long id;
    
    @Schema(name = "日志名称")
    private String logName;
    
    @Schema(name = "请求方法")
    private String requestMethod;
    
    @Schema(name = "执行时间(毫秒)")
    private Integer executionTime;
    
    @Schema(name = "开始时间")
    private LocalDateTime startTime;
    
    @Schema(name = "结束时间")
    private LocalDateTime endTime;
    
    @Schema(name = "线程ID")
    private Long threadId;
    
    @Schema(name = "线程名")
    private String threadName;
    
    @Schema(name = "进程ID")
    private Long processId;
    
    @Schema(name = "请求信息")
    private WebRequest webRequest;
    
    @Schema(name = "方法调用信息")
    private MethodCallDetail methodCallDetail;
    
    @Schema(name = "设备信息")
    private Devices devices;
    
    @Schema(name = "异常信息")
    private ExceptionReport exceptionReport;
    
    @Getter
    @Setter
    public static class WebRequest {
        @Schema(name = "接口路径")
        private String mappingPath;
        
        @Schema(name = "接口路径参数")
        private String fullPath;
        
        @Schema(name = "请求host")
        private String remoteHost;
    }
    
    @Getter
    @Setter
    public static class Devices {
        @Schema(name = "User-Agent")
        private String userAgentContent;
        
        @Schema(name = "是否为移动平台")
        private Boolean mobile;
        
        @Schema(name = "浏览器类型")
        private String browser;
        
        @Schema(name = "浏览器版本")
        private String browserVersion;
        
        @Schema(name = "平台类型")
        private String platform;
        
        @Schema(name = "系统类型")
        private String os;
        
        @Schema(name = "系统版本")
        private String osVersion;
        
        @Schema(name = "渲染引擎类型")
        private String renderingEngine;
        
        @Schema(name = "渲染引擎版本")
        private String renderingEngineVersion;
    }
    
    @Setter
    @Getter
    public static class ExceptionReport {
        @Schema(name = "异常信息")
        private String exceptionMessage;
        
        @Schema(name = "异常栈")
        private String exceptionStack;
        
        @Schema(name = "方法行号")
        private Integer exceptionLineNumber;
        
        @Schema(name = "抛出异常类")
        private String exceptionClassName;
    }
    
    @Setter
    @Getter
    public static class MethodCallDetail implements Serializable {
        /**
         * 请求参数数据
         */
        private String methodParamsData;
        /**
         * 入参列表
         */
        private String methodParamsType;
        /**
         * 返回参数列表
         */
        private String methodResultParamType;
        /**
         * 全类方法名
         */
        private String methodName;
    }
}
