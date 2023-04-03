package org.core.model.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("用户收藏专辑表")
public class TbUserAlbumPojoReq implements Serializable {
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
