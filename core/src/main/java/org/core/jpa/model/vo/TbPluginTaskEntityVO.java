package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Schema(title = "保存 插件任务表")
public class TbPluginTaskEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 任务ID
     */
    @NotNull(message = "id can not null")
    @Schema(name = "任务ID")
    private Long id;
    
    
    /**
     * 插件ID
     */
    @NotNull(message = "pluginId can not null")
    @Schema(name = "插件ID")
    private Long pluginId;
    
    
    /**
     * 当前任务执行状态,0: stop, 1: run, 2: error
     */
    @NotNull(message = "status can not null")
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
    @NotNull(message = "userId can not null")
    @Schema(name = "用户创建ID")
    private Long userId;
    
    
    /**
     * 创建时间
     */
    @NotNull(message = "createTime can not null")
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 更新时间
     */
    @NotNull(message = "updateTime can not null")
    @Schema(name = "更新时间")
    private LocalDateTime updateTime;
    
}
