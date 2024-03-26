package org.core.jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "sys_log")
public class SysLogEntity {
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Size(max = 256)
    @Column(name = "log_name", length = 256)
    private String logName;
    
    @Size(max = 128)
    @Column(name = "thread_name", length = 128)
    private String threadName;
    
    @Column(name = "thread_id")
    private Long threadId;
    
    @Column(name = "process_id")
    private Long processId;
    
    @Column(name = "execution_time")
    private Integer executionTime;
    
    @Column(name = "start_time")
    private Instant startTime;
    
    @Column(name = "end_time")
    private Instant endTime;
    
    @Lob
    @Column(name = "method_params_data")
    private String methodParamsData;
    
    @Size(max = 512)
    @Column(name = "method_params_type", length = 512)
    private String methodParamsType;
    
    @Size(max = 512)
    @Column(name = "method_result_param_type", length = 512)
    private String methodResultParamType;
    
    @Size(max = 512)
    @Column(name = "methodName", length = 512)
    private String methodName;
    
    @Size(max = 24)
    @Column(name = "request_method", length = 24)
    private String requestMethod;
    
    @Size(max = 512)
    @Column(name = "full_path", length = 512)
    private String fullPath;
    
    @Size(max = 128)
    @Column(name = "remote_host", length = 128)
    private String remoteHost;
    
    @Size(max = 512)
    @Column(name = "mapping_path", length = 512)
    private String mappingPath;
    
    @Size(max = 512)
    @Column(name = "user_agent_content", length = 512)
    private String userAgentContent;
    
    @Column(name = "mobile")
    private Boolean mobile;
    
    @Size(max = 128)
    @Column(name = "browser", length = 128)
    private String browser;
    
    @Size(max = 128)
    @Column(name = "browser_version", length = 128)
    private String browserVersion;
    
    @Size(max = 128)
    @Column(name = "platform", length = 128)
    private String platform;
    
    @Size(max = 128)
    @Column(name = "os", length = 128)
    private String os;
    
    @Size(max = 128)
    @Column(name = "os_version", length = 128)
    private String osVersion;
    
    @Size(max = 128)
    @Column(name = "rendering_engine", length = 128)
    private String renderingEngine;
    
    @Size(max = 128)
    @Column(name = "rendering_engine_version", length = 128)
    private String renderingEngineVersion;
    
    @Size(max = 512)
    @Column(name = "exception_message", length = 512)
    private String exceptionMessage;
    
    @Lob
    @Column(name = "exception_stack")
    private String exceptionStack;
    
    @Column(name = "exception_line_number")
    private Integer exceptionLineNumber;
    
    @Size(max = 256)
    @Column(name = "exception_class_name", length = 256)
    private String exceptionClassName;
    
    @Column(name = "update_time")
    private Instant updateTime;
    
    @Column(name = "create_time")
    private Instant createTime;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        SysLogEntity that = (SysLogEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(logName, that.logName)
                                  .append(threadName, that.threadName)
                                  .append(threadId, that.threadId)
                                  .append(processId, that.processId)
                                  .append(executionTime, that.executionTime)
                                  .append(startTime, that.startTime)
                                  .append(endTime, that.endTime)
                                  .append(methodParamsData, that.methodParamsData)
                                  .append(methodParamsType, that.methodParamsType)
                                  .append(methodResultParamType, that.methodResultParamType)
                                  .append(methodName, that.methodName)
                                  .append(requestMethod, that.requestMethod)
                                  .append(fullPath, that.fullPath)
                                  .append(remoteHost, that.remoteHost)
                                  .append(mappingPath, that.mappingPath)
                                  .append(userAgentContent, that.userAgentContent)
                                  .append(mobile, that.mobile)
                                  .append(browser, that.browser)
                                  .append(browserVersion, that.browserVersion)
                                  .append(platform, that.platform)
                                  .append(os, that.os)
                                  .append(osVersion, that.osVersion)
                                  .append(renderingEngine, that.renderingEngine)
                                  .append(renderingEngineVersion, that.renderingEngineVersion)
                                  .append(exceptionMessage, that.exceptionMessage)
                                  .append(exceptionStack, that.exceptionStack)
                                  .append(exceptionLineNumber, that.exceptionLineNumber)
                                  .append(exceptionClassName, that.exceptionClassName)
                                  .append(updateTime, that.updateTime)
                                  .append(createTime, that.createTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(logName)
                                          .append(threadName)
                                          .append(threadId)
                                          .append(processId)
                                          .append(executionTime)
                                          .append(startTime)
                                          .append(endTime)
                                          .append(methodParamsData)
                                          .append(methodParamsType)
                                          .append(methodResultParamType)
                                          .append(methodName)
                                          .append(requestMethod)
                                          .append(fullPath)
                                          .append(remoteHost)
                                          .append(mappingPath)
                                          .append(userAgentContent)
                                          .append(mobile)
                                          .append(browser)
                                          .append(browserVersion)
                                          .append(platform)
                                          .append(os)
                                          .append(osVersion)
                                          .append(renderingEngine)
                                          .append(renderingEngineVersion)
                                          .append(exceptionMessage)
                                          .append(exceptionStack)
                                          .append(exceptionLineNumber)
                                          .append(exceptionClassName)
                                          .append(updateTime)
                                          .append(createTime)
                                          .toHashCode();
    }
}