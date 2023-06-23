package org.core.jpa.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@ApiModel("保存 歌手和专辑中间表")
public class TbAlbumArtistEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 专辑ID
     */
    @NotNull(message = "albumId can not null")
    @ApiModelProperty("专辑ID")
    private Long albumId;
    
    
    /**
     * 歌手ID
     */
    @NotNull(message = "artistId can not null")
    @ApiModelProperty("歌手ID")
    private Long artistId;
    
}
