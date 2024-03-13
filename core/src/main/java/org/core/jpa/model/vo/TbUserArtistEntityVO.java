package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;


@Data
@Schema(title = "保存 用户关注歌曲家")
public class TbUserArtistEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 用户ID
     */
    @NotNull(message = "userId can not null")
    @Schema(name = "用户ID")
    private Long userId;
    
    
    /**
     * 歌手ID
     */
    @NotNull(message = "artistId can not null")
    @Schema(name = "歌手ID")
    private Long artistId;
    
}
