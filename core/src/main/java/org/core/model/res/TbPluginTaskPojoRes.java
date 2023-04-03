package org.core.model.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@ApiModel("保存 插件任务表")
public class TbPluginTaskPojoRes implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 任务ID
     */
    @NotNull(message = "id can not null")
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
    @NotNull(message = "userId can not null")
    @ApiModelProperty("用户创建ID")
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
