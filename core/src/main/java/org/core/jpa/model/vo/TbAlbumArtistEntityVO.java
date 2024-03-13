package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;


@Data
@Schema(title = "保存 歌手和专辑中间表")
public class TbAlbumArtistEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 专辑ID
     */
    @NotNull(message = "albumId can not null")
    @Schema(name = "专辑ID")
    private Long albumId;
    
    
    /**
     * 歌手ID
     */
    @NotNull(message = "artistId can not null")
    @Schema(name = "歌手ID")
    private Long artistId;
    
}
