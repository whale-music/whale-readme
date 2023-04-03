package org.core.model.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@ApiModel("保存 插件消息表")
public class TbPluginMsgPojoRes implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 插件消息ID
     */
    @NotNull(message = "id can not null")
    @ApiModelProperty("插件消息ID")
    private Long id;
    
    
    /**
     * 插件ID
     */
    @NotNull(message = "pluginId can not null")
    @ApiModelProperty("插件ID")
    private Long pluginId;
    
    
    /**
     * 任务ID
     */
    @NotNull(message = "taskId can not null")
    @ApiModelProperty("任务ID")
    private Long taskId;
    
    
    /**
     * 用户ID
     */
    @NotNull(message = "userId can not null")
    @ApiModelProperty("用户ID")
    private Long userId;
    
    
    /**
     * 插件运行消息
     */
    @ApiModelProperty("插件运行消息")
    private String msg;
    
    
    /**
     * 创建时间
     */
    @NotNull(message = "createTime can not null")
    @ApiModelProperty("创建时间")
    private Long createTime;
    
    
    /**
     * 更新时间
     */
    @NotNull(message = "updateTime can not null")
    @ApiModelProperty("更新时间")
    private Long updateTime;
    
}
