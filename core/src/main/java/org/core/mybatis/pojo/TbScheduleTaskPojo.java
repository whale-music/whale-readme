package org.core.mybatis.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 定时任务表(TbScheduleTask)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:32
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_schedule_task")
@Schema(name = "TbScheduleTask", description = "定时任务表")
public class TbScheduleTaskPojo extends Model<TbScheduleTaskPojo> implements Serializable {
    public static final long serialVersionUID = -33030286758411000L;
    
    @Schema(title = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "定时任务名")
    @TableField("name")
    private String name;
    
    @Schema(title = "插件ID")
    @TableField("plugin_id")
    private Long pluginId;
    
    @Schema(title = "cron表达式")
    @TableField("cron")
    private String cron;
    
    @Schema(title = "插件入参json格式")
    @TableField("params")
    private String params;
    
    @Schema(title = "是否执行(true执行, false不执行)")
    @TableField(value = "status", insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private Boolean status;
    
    @Schema(title = "用户ID")
    @TableField("user_id")
    private Long userId;
    
    @Schema(title = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @Schema(title = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    
}

