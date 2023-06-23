package org.core.mybatis.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 定时任务表
 * </p>
 *
 * @author Sakura
 * @since 2023-05-17
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_schedule_task")
@ApiModel(value = "TbSchedulePojo对象", description = "定时任务表")
public class TbScheduleTaskPojo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @ApiModelProperty("定时任务名")
    @TableField("name")
    private String name;
    
    @ApiModelProperty("插件ID")
    @TableField("plugin_id")
    private Long pluginId;
    
    @ApiModelProperty("cron表达式")
    @TableField("cron")
    private String cron;
    
    @ApiModelProperty("插件入参json格式")
    @TableField("params")
    private String params;
    
    @ApiModelProperty("是否执行(true执行, false不执行)")
    @TableField(value = "status", insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private Boolean status;
    
    @ApiModelProperty("用户ID")
    @TableField("user_id")
    private Long userId;
    
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    
}
