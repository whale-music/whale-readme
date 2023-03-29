package org.core.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
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
@Accessors(chain = true)
@TableName("tb_plugin_task")
@ApiModel(value = "TbPluginTaskPojo对象", description = "插件任务表")
public class TbPluginTaskPojo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("任务ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @ApiModelProperty("任务名")
    @TableField("task_name")
    private String taskName;
    
    @ApiModelProperty("用户创建ID")
    @TableField("user_id")
    private Long userId;
    
    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @ApiModelProperty("更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    
}
