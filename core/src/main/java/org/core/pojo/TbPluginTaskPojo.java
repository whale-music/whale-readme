package org.core.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 插件任务表
 * </p>
 *
 * @author Sakura
 * @since 2023-03-29
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("tb_plugin_task")
@ApiModel(value = "TbPluginTaskPojo对象", description = "插件任务表")
public class TbPluginTaskPojo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("任务ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @ApiModelProperty("插件ID")
    @TableField("plugin_id")
    private Long pluginId;
    
    @ApiModelProperty("当前任务执行状态,0: stop, 1: run, 2: error")
    @TableField("status")
    private Short status;
    
    @ApiModelProperty("用户创建ID")
    @TableField("user_id")
    private Long userId;
    
    @ApiModelProperty("插件入参")
    @TableField("params")
    private String params;
    
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    
}
