package org.core.jpa.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Data
@ApiModel("保存 插件表")
public class TbPluginEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 插件ID
     */
    @NotNull(message = "id can not null")
    @ApiModelProperty("插件ID")
    private Long id;
    
    
    /**
     * 插件名称
     */
    @NotNull(message = "pluginName can not null")
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
    @NotNull(message = "type can not null")
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
    @NotNull(message = "userId can not null")
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
    @NotNull(message = "createTime can not null")
    @ApiModelProperty("创建时间")
    private Date createTime;
    
    
    /**
     * 更新时间
     */
    @NotNull(message = "updateTime can not null")
    @ApiModelProperty("更新时间")
    private Date updateTime;
    
}
