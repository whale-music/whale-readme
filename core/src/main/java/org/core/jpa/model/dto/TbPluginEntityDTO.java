package org.core.jpa.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("插件表")
public class TbPluginEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 插件ID
     */
    @ApiModelProperty("插件ID")
    private Long id;
    
    
    /**
     * 插件名称
     */
    @ApiModelProperty("插件名称")
    private String pluginName;
    
    
    /**
     * 插件创建者
     */
    @ApiModelProperty("插件创建者")
    private String createName;
    
    
    /**
     * 插件类型
     */
    @ApiModelProperty("插件类型")
    private String type;
    
    
    /**
     * 插件代码
     */
    @ApiModelProperty("插件代码")
    private String code;
    
    
    /**
     * 插件创建者
     */
    @ApiModelProperty("插件创建者")
    private Long userId;
    
    
    /**
     * 插件描述
     */
    @ApiModelProperty("插件描述")
    private String description;
    
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    
}
