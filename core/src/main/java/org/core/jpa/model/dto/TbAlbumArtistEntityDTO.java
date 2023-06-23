package org.core.jpa.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("歌手和专辑中间表")
public class TbAlbumArtistEntityDTO implements Serializable {
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
