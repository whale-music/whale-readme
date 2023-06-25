package org.core.jpa.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@ApiModel("保存 插件任务表")
public class TbPluginTaskEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 任务ID
     */
    @NotNull(message = "id can not null")
    @ApiModelProperty("任务ID")
    private Long id;
    
    
    /**
     * 插件ID
     */
    @NotNull(message = "pluginId can not null")
    @ApiModelProperty("插件ID")
    private Long pluginId;
    
    
    /**
     * 当前任务执行状态,0: stop, 1: run, 2: error
     */
    @NotNull(message = "status can not null")
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
    @NotNull(message = "userId can not null")
    @ApiModelProperty("用户创建ID")
    private Long userId;
    
    
    /**
     * 创建时间
     */
    @NotNull(message = "createTime can not null")
    @ApiModelProperty("创建时间")
    private Date createTime;
    
    
    /**
     * 更新时间
     */
    @NotNull(message = "updateTime can not null")
    @ApiModelProperty("更新时间")
    private Date updateTime;
    
}
