package org.core.model.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("用户关注歌曲家")
public class TbUserArtistPojoReq implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;
    
    
    /**
     * 歌手ID
     */
    @ApiModelProperty("歌手ID")
    private Long artistId;
    
}
