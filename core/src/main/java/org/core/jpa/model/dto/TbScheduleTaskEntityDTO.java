package org.core.jpa.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("定时任务表")
public class TbScheduleTaskEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;
    
    
    /**
     * 定时任务名
     */
    @ApiModelProperty("定时任务名")
    private String name;
    
    
    /**
     * 插件ID
     */
    @ApiModelProperty("插件ID")
    private Long pluginId;
    
    
    /**
     * cron表达式
     */
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
    @ApiModelProperty("用户ID")
    private Long userId;
    
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    
    
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
    
}
