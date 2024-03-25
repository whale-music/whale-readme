package org.core.mybatis.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_log")
@Schema(name = "SysUser", description = "系统日志实体类")
public class SysLogPojo extends Model<SysLogPojo> implements Serializable {
    
    @Schema(description = "日志ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(name = "线程名称")
    @TableField("thread_name")
    private String threadName;
    
    @Schema(name = "线程ID")
    @TableField("thread_id")
    private Long threadId;
    
    @Schema(name = "进程ID")
    @TableField("process_id")
    private Long processId;
    
    @TableField("execution_time")
    @Schema(description = "执行时间（毫秒）")
    private Integer executionTime;
    
    @Schema(description = "开始时间")
    @TableField("start_time")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间")
    @TableField("end_time")
    private LocalDateTime endTime;
    
    @Schema(description = "请求参数数据")
    @TableField("method_params_data")
    private String methodParamsData;
    
    @Schema(description = "入参列表")
    @TableField("method_params_type")
    private String methodParamsType;
    
    @Schema(description = "返回参数列表")
    @TableField("method_result_param_type")
    private String methodResultParamType;
    
    @Schema(description = "全类方法名")
    @TableField("methodName")
    private String methodName;
    
    @Schema(description = "请求方法")
    @TableField("request_method")
    private String requestMethod;
    
    @Schema(description = "接口路径参数")
    @TableField("full_path")
    private String fullPath;
    
    @Schema(description = "请求host")
    @TableField("remote_host")
    private String remoteHost;
    
    @Schema(description = "接口路径")
    @TableField("mapping_path")
    private String mappingPath;
    
    @Schema(description = "User-Agent")
    @TableField("user_agent_content")
    private String userAgentContent;
    
    @Schema(description = "是否为移动平台")
    @TableField("mobile")
    private Character mobile;
    
    @Schema(description = "浏览器类型")
    @TableField("browser")
    private String browser;
    
    @Schema(description = "浏览器版本")
    @TableField("browser_version")
    private String browserVersion;
    
    @Schema(description = "平台类型")
    @TableField("platform")
    private String platform;
    
    @Schema(description = "系统类型")
    @TableField("os")
    private String os;
    
    @Schema(description = "系统版本")
    @TableField("os_version")
    private String osVersion;
    
    @Schema(description = "引擎类型")
    @TableField("rendering_engine")
    private String renderingEngine;
    
    @Schema(description = "引擎版本")
    @TableField("rendering_engine_version")
    private String renderingEngineVersion;
    
    @Schema(description = "异常信息")
    @TableField("exception_message")
    private String exceptionMessage;
    
    @Schema(description = "异常栈")
    @TableField("exception_stack")
    private String exceptionStack;
    
    @Schema(description = "异常方法行号")
    @TableField("exception_line_number")
    private Integer exceptionLineNumber;
    
    @Schema(description = "抛出异常类")
    @TableField("exception_class_name")
    private String exceptionClassName;
    
    @Schema(description = "更新日期")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @Schema(description = "创建日期")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
}
