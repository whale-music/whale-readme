package org.core.jpa.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel("保存 用户关注歌曲家")
public class TbUserArtistEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 用户ID
     */
    @NotNull(message = "userId can not null")
    @ApiModelProperty("用户ID")
    private Long userId;
    
    
    /**
     * 歌手ID
     */
    @NotNull(message = "artistId can not null")
    @ApiModelProperty("歌手ID")
    private Long artistId;
    
}
