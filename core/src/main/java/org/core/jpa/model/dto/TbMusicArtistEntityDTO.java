package org.core.jpa.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("音乐与歌手中间表")
public class TbMusicArtistEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 音乐ID
     */
    @ApiModelProperty("音乐ID")
    private Long musicId;
    
    
    /**
     * 艺术家ID
     */
    @ApiModelProperty("艺术家ID")
    private Long artistId;
    
}
