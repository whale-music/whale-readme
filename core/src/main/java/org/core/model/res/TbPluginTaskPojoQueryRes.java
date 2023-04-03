package org.core.model.res;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("自定义查询 插件任务表")
public class TbPluginTaskPojoQueryRes implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 任务ID
     */
    @ApiModelProperty("任务ID")
    private Long id;
    
    
    /**
     * 任务名
     */
    @ApiModelProperty("任务名")
    private String taskName;
    
    
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
