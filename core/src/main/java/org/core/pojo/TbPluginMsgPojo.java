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

/**
 * <p>
 * 插件消息表
 * </p>
 *
 * @author Sakura
 * @since 2023-03-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_plugin_msg")
@ApiModel(value = "TbPluginMsgPojo对象", description = "插件消息表")
public class TbPluginMsgPojo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("插件消息ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @ApiModelProperty("插件ID")
    @TableField("plugin_id")
    private Long pluginId;
    
    @ApiModelProperty("任务ID")
    @TableField("task_id")
    private Long taskId;
    
    @ApiModelProperty("用户ID")
    @TableField("user_id")
    private Long userId;
    
    @ApiModelProperty("插件运行消息")
    @TableField("msg")
    private String msg;
    
    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private Long createTime;
    
    @ApiModelProperty("更新时间")
    @TableField("update_time")
    private Long updateTime;
    
    
}
