package org.core.jpa.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@ApiModel("保存 定时任务表")
public class TbScheduleTaskEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * ID
     */
    @NotNull(message = "id can not null")
    @ApiModelProperty("ID")
    private Long id;
    
    
    /**
     * 定时任务名
     */
    @NotNull(message = "name can not null")
    @ApiModelProperty("定时任务名")
    private String name;
    
    
    /**
     * 插件ID
     */
    @NotNull(message = "pluginId can not null")
    @ApiModelProperty("插件ID")
    private Long pluginId;
    
    
    /**
     * cron表达式
     */
    @NotNull(message = "cron can not null")
    @ApiModelProperty("cron表达式")
    private String cron;
    
    
    /**
     * 插件入参json格式
     */
    @ApiModelProperty("插件入参json格式")
    private String params;
    
    
    /**
     * 是否执行(true执行, false不执行)
     */
    @ApiModelProperty("是否执行(true执行, false不执行)")
    private Boolean status;
    
    
    /**
     * 用户ID
     */
    @NotNull(message = "userId can not null")
    @ApiModelProperty("用户ID")
    private Long userId;
    
    
    /**
     * 创建时间
     */
    @NotNull(message = "createTime can not null")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 更新时间
     */
    @NotNull(message = "updateTime can not null")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    
}
