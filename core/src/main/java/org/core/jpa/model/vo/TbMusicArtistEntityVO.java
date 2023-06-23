package org.core.jpa.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@ApiModel("保存 音乐与歌手中间表")
public class TbMusicArtistEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 音乐ID
     */
    @NotNull(message = "musicId can not null")
    @ApiModelProperty("音乐ID")
    private Long musicId;
    
    
    /**
     * 艺术家ID
     */
    @NotNull(message = "artistId can not null")
    @ApiModelProperty("艺术家ID")
    private Long artistId;
    
}
