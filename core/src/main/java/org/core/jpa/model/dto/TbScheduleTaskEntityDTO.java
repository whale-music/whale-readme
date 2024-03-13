package org.core.jpa.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(title = "定时任务表")
public class TbScheduleTaskEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * ID
     */
    @Schema(name = "ID")
    private Long id;
    
    
    /**
     * 定时任务名
     */
    @Schema(name = "定时任务名")
    private String name;
    
    
    /**
     * 插件ID
     */
    @Schema(name = "插件ID")
    private Long pluginId;
    
    
    /**
     * cron表达式
     */
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
    @Schema(name = "用户ID")
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
