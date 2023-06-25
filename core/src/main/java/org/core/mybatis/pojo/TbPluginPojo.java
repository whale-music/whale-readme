package org.core.mybatis.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * 插件表
 * </p>
 *
 * @author Sakura
 * @since 2023-06-25
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("tb_plugin")
@ApiModel(value = "TbPluginPojo对象", description = "插件表")
public class TbPluginPojo extends Model<TbPluginPojo> implements Serializable {

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
    
    @ApiModelProperty("插件类型")
    @TableField("type")
    private String type;
    
    @ApiModelProperty("插件代码")
    @TableField("code")
    private String code;
    
    @ApiModelProperty("插件创建者")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("插件描述")
    @TableField("description")
    private String description;
    
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    
}
