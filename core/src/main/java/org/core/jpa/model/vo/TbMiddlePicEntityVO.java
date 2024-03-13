package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;


@Data
@Schema(title = "保存 封面中间表")
public class TbMiddlePicEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "id can not null")
    private Long id;
    
    
    /**
     * 中间表
     */
    @NotNull(message = "middleId can not null")
    @Schema(name = "中间表")
    private Long middleId;
    
    
    /**
     * 封面ID
     */
    @NotNull(message = "picId can not null")
    @Schema(name = "封面ID")
    private Long picId;
    
    
    /**
     * 封面类型,歌单-1,专辑-2,歌手-3,歌手-3.用户头像-4,用户背景-5, mv标签-6
     */
    @Schema(name = "封面类型,歌单-1,专辑-2,歌手-3,歌手-3.用户头像-4,用户背景-5, mv标签-6")
    private Integer type;
    
}
