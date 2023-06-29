package org.core.mybatis.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 插件任务表(TbPluginTask)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:32
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_plugin_task")
@Schema(name = "TbPluginTask", description = "插件任务表")
public class TbPluginTaskPojo extends Model<TbPluginTaskPojo> implements Serializable {
    public static final long serialVersionUID = -24393927956368288L;
    
    @Schema(title = "任务ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "插件ID")
    @TableField("plugin_id")
    private Long pluginId;
    
    @Schema(title = "当前任务执行状态,0: stop, 1: run, 2: error")
    @TableField("status")
    private Byte status;
    
    @Schema(title = "插件入参")
    @TableField("params")
    private String params;
    
    @Schema(title = "用户创建ID")
    @TableField("user_id")
    private Long userId;
    
    @Schema(title = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @Schema(title = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    
}

