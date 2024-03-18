package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Schema(title = "保存 定时任务表")
public class TbScheduleTaskEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * ID
     */
    @NotNull(message = "id can not null")
    @Schema(name = "ID")
    private Long id;
    
    
    /**
     * 定时任务名
     */
    @NotNull(message = "name can not null")
    @Schema(name = "定时任务名")
    private String name;
    
    
    /**
     * 插件ID
     */
    @NotNull(message = "pluginId can not null")
    @Schema(name = "插件ID")
    private Long pluginId;
    
    
    /**
     * cron表达式
     */
    @NotNull(message = "cron can not null")
    @Schema(name = "cron表达式")
    private String cron;
    
    
    /**
     * 插件入参json格式
     */
    @Schema(name = "插件入参json格式")
    private String params;
    
    
    /**
     * 是否执行(true执行, false不执行)
     */
    @Schema(name = "是否执行(true执行, false不执行)")
    private Boolean status;
    
    
    /**
     * 用户ID
     */
    @NotNull(message = "userId can not null")
    @Schema(name = "用户ID")
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
