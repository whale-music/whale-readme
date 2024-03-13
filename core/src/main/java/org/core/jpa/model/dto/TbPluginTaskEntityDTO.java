package org.core.jpa.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(title = "插件任务表")
public class TbPluginTaskEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 任务ID
     */
    @Schema(name = "任务ID")
    private Long id;
    
    
    /**
     * 插件ID
     */
    @Schema(name = "插件ID")
    private Long pluginId;
    
    
    /**
     * 当前任务执行状态,0: stop, 1: run, 2: error
     */
    @Schema(name = "当前任务执行状态,0: stop, 1: run, 2: error")
    private Integer status;
    
    
    /**
     * 插件入参
     */
    @Schema(name = "插件入参")
    private String params;
    
    
    /**
     * 用户创建ID
     */
    @Schema(name = "用户创建ID")
    private Long userId;
    
    
    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 更新时间
     */
    @Schema(name = "更新时间")
    private LocalDateTime updateTime;
    
}
