package org.core.model.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("歌手和专辑中间表")
public class TbAlbumArtistPojoReq implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 专辑ID
     */
    @ApiModelProperty("专辑ID")
    private Long albumId;
    
    
    /**
     * 歌手ID
     */
    @ApiModelProperty("歌手ID")
    private Long artistId;
    
}
