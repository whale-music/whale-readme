package org.core.jpa.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("音乐来源")
public class TbOriginEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    
    
    /**
     * 音乐ID
     */
    @ApiModelProperty("音乐ID")
    private Long musicId;
    
    
    /**
     * 源地址ID
     */
    @ApiModelProperty("源地址ID")
    private Long resourceId;
    
    
    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String origin;
    
    
    /**
     * 来源地址
     */
    @ApiModelProperty("来源地址")
    private String originUrl;
    
}
