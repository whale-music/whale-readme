package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;


@Data
@Schema(title = "保存 音乐与歌手中间表")
public class TbMusicArtistEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 音乐ID
     */
    @NotNull(message = "musicId can not null")
    @Schema(name = "音乐ID")
    private Long musicId;
    
    
    /**
     * 艺术家ID
     */
    @NotNull(message = "artistId can not null")
    @Schema(name = "艺术家ID")
    private Long artistId;
    
}
