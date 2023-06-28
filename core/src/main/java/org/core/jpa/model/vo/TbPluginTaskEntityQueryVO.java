package org.core.jpa.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("自定义查询 插件任务表")
public class TbPluginTaskEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 任务ID
     */
    @ApiModelProperty("任务ID")
    private Long id;
    
    
    /**
     * 插件ID
     */
    @ApiModelProperty("插件ID")
    private Long pluginId;
    
    
    /**
     * 当前任务执行状态,0: stop, 1: run, 2: error
     */
    @ApiModelProperty("当前任务执行状态,0: stop, 1: run, 2: error")
    private Integer status;
    
    
    /**
     * 插件入参
     */
    @ApiModelProperty("插件入参")
    private String params;
    
    
    /**
     * 用户创建ID
     */
    @ApiModelProperty("用户创建ID")
    private Long userId;
    
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    
}
