package org.core.jpa.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("插件消息表")
public class TbPluginMsgEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 插件消息ID
     */
    @ApiModelProperty("插件消息ID")
    private Long id;
    
    
    /**
     * 插件ID
     */
    @ApiModelProperty("插件ID")
    private Long pluginId;
    
    
    /**
     * 任务ID
     */
    @ApiModelProperty("任务ID")
    private Long taskId;
    
    
    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;
    
    
    /**
     * 插件消息等级,0 info 1 debug 2 warn 3 error
     */
    @ApiModelProperty("插件消息等级,0 info 1 debug 2 warn 3 error")
    private Integer level;
    
    
    /**
     * 插件运行消息
     */
    @ApiModelProperty("插件运行消息")
    private String msg;
    
    
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
