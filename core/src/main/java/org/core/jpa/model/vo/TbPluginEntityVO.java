package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Schema(title = "保存 插件表")
public class TbPluginEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 插件ID
     */
    @NotNull(message = "id can not null")
    @Schema(name = "插件ID")
    private Long id;
    
    
    /**
     * 插件名称
     */
    @NotNull(message = "pluginName can not null")
    @Schema(name = "插件名称")
    private String pluginName;
    
    
    /**
     * 插件创建者
     */
    @Schema(name = "插件创建者")
    private String createName;
    
    
    /**
     * 插件类型
     */
    @NotNull(message = "type can not null")
    @Schema(name = "插件类型")
    private String type;
    
    
    /**
     * 插件代码
     */
    @Schema(name = "插件代码")
    private String code;
    
    
    /**
     * 插件创建者
     */
    @NotNull(message = "userId can not null")
    @Schema(name = "插件创建者")
    private Long userId;
    
    
    /**
     * 插件描述
     */
    @Schema(name = "插件描述")
    private String description;
    
    
    /**
     * 创建时间
     */
    @NotNull(message = "createTime can not null")
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 更新时间
     */
    @NotNull(message = "updateTime can not null")
    @Schema(name = "更新时间")
    private LocalDateTime updateTime;
    
}
