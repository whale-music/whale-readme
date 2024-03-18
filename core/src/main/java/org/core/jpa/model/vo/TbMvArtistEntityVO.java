package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;


@Data
@Schema(title = "保存 mv和歌手中间表")
public class TbMvArtistEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "mvId can not null")
    private Long mvId;
    
    @NotNull(message = "artistId can not null")
    private Long artistId;
    
}
