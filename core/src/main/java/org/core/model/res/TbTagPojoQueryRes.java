package org.core.model.res;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("自定义查询 标签表（风格）")
public class TbTagPojoQueryRes implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    
    /**
     * 风格（标签）
     */
    @ApiModelProperty("风格（标签）")
    private String tagName;
    
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
    
}
