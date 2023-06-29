package org.core.mybatis.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 插件表(TbPlugin)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:32
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_plugin")
@Schema(name = "TbPlugin", description = "插件表")
public class TbPluginPojo extends Model<TbPluginPojo> implements Serializable {
    public static final long serialVersionUID = 330409536410303640L;
    
    @Schema(title = "插件ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "插件名称")
    @TableField("plugin_name")
    private String pluginName;
    
    @Schema(title = "插件创建者")
    @TableField("create_name")
    private String createName;
    
    @Schema(title = "插件类型")
    @TableField("type")
    private String type;
    
    @Schema(title = "插件代码")
    @TableField("code")
    private String code;
    
    @Schema(title = "插件创建者")
    @TableField("user_id")
    private Long userId;
    
    @Schema(title = "插件描述")
    @TableField("description")
    private String description;
    
    @Schema(title = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @Schema(title = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    
}

