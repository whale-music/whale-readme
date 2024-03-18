package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "自定义查询 封面中间表")
public class TbMiddlePicEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    
    /**
     * 中间表
     */
    @Schema(name = "中间表")
    private Long middleId;
    
    
    /**
     * 封面ID
     */
    @Schema(name = "封面ID")
    private Long picId;
    
    
    /**
     * 封面类型,歌单-1,专辑-2,歌手-3,歌手-3.用户头像-4,用户背景-5, mv标签-6
     */
    @Schema(name = "封面类型,歌单-1,专辑-2,歌手-3,歌手-3.用户头像-4,用户背景-5, mv标签-6")
    private Integer type;
    
}
