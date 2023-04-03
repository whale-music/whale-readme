package org.core.model.res;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("自定义查询 用户收藏专辑表")
public class TbUserAlbumPojoQueryRes implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;
    
    
    /**
     * 专辑ID
     */
    @ApiModelProperty("专辑ID")
    private Long albumId;
    
}
