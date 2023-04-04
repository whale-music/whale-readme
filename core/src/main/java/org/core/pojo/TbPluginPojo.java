package org.core.pojo;

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
 * 插件表
 * </p>
 *
 * @author Sakura
 * @since 2023-03-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_plugin")
@ApiModel(value = "TbPluginPojo对象", description = "插件表")
public class TbPluginPojo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("插件ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @ApiModelProperty("插件名称")
    @TableField("plugin_name")
    private String pluginName;
    
    @ApiModelProperty("插件创建者")
    @TableField("create_name")
    private String createName;
    
    @ApiModelProperty("插件代码")
    @TableField("code")
    private String code;
    
    @ApiModelProperty("插件描述")
    @TableField("description")
    private String description;
    
    @ApiModelProperty("插件创建者")
    @TableField("user_id")
    private Long userId;
    
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    
}