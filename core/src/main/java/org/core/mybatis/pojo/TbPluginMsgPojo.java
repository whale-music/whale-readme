package org.core.mybatis.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 插件消息表(TbPluginMsg)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:32
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_plugin_msg")
@Schema(name = "TbPluginMsg", description = "插件消息表")
public class TbPluginMsgPojo extends Model<TbPluginMsgPojo> implements Serializable {
    public static final long serialVersionUID = -41807181288121589L;
    
    @Schema(title = "插件消息ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "插件ID")
    @TableField("plugin_id")
    private Long pluginId;
    
    @Schema(title = "任务ID")
    @TableField("task_id")
    private Long taskId;
    
    @Schema(title = "用户ID")
    @TableField("user_id")
    private Long userId;
    
    @Schema(title = "插件消息等级,0 info 1 debug 2 warn 3 error")
    @TableField("level")
    private Byte level;
    
    @Schema(title = "插件运行消息")
    @TableField("msg")
    private String msg;
    
    @Schema(title = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @Schema(title = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    
}

