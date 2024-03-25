package org.core.common.weblog.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebLogRecord implements Serializable {
    /**
     * 日志名称
     */
    private String logName;
    /**
     * 执行时间(毫秒)
     */
    private Integer executionTime;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 线程ID
     */
    private Long threadId;
    
    /**
     * 线程名
     */
    private String threadName;
    
    /**
     * 进程ID
     */
    private Long processId;
    
    /**
     * 方法调用信息
     */
    private MethodCallDetail methodCallDetail;
    
    /**
     * 请求信息
     */
    private WebRequest webRequest;
    
    /**
     * 报错信息
     */
    private ExceptionReport exceptionReport;
    
    public boolean isSuccess() {
        return exceptionReport == null;
    }
    
    @Getter
    @Setter
    public static class ExceptionReport implements Serializable {
        /**
         * 异常信息
         */
        private String exceptionMessage;
        
        /**
         * 异常栈
         */
        private String exceptionStack;
        
        /**
         * 方法行号
         */
        private Integer exceptionLineNumber;
        
        /**
         * 抛出异常类
         */
        private String exceptionClassName;
    }
    
    @Getter
    @Setter
    public static class WebRequest implements Serializable {
        /**
         * 请求方法
         */
        private String requestMethod;
        /**
         * 接口路径参数
         */
        private String fullPath;
        /**
         * 请求host
         */
        private String remoteHost;
        /**
         * 接口路径
         */
        private String mappingPath;
        /**
         * 请求 user-agent
         */
        private UserAgent userAgent;
        
        @Data
        public static class UserAgent implements Serializable {
            /**
             * User-Agent
             */
            private String userAgentContent;
            /**
             * 是否为移动平台
             */
            private boolean mobile;
            /**
             * 浏览器类型
             */
            private String browser;
            /**
             * 浏览器版本
             */
            private String browserVersion;
            
            /**
             * 平台类型
             */
            private String platform;
            
            /**
             * 系统类型
             */
            private String os;
            /**
             * 系统版本
             */
            private String osVersion;
            /**
             * 渲染引擎类型
             */
            private String renderingEngine;
            /**
             * 渲染引擎版本
             */
            private String renderingEngineVersion;
        }
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
