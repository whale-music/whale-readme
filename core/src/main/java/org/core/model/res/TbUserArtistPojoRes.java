package org.core.model.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@ApiModel("保存 用户关注歌曲家")
public class TbUserArtistPojoRes implements Serializable {
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
