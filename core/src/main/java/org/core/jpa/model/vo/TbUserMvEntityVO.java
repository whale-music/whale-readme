package org.core.jpa.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel("保存 用户收藏mv")
public class TbUserMvEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 用户ID
     */
    @NotNull(message = "userId can not null")
    @ApiModelProperty("用户ID")
    private Long userId;
    
    
    /**
     * MV ID
     */
    @NotNull(message = "mvId can not null")
    @ApiModelProperty("MV ID")
    private Long mvId;
    
}
