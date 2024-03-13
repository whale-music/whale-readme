package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "自定义查询 歌单风格中间表")
public class TbMiddleTagEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    
    /**
     * 中间ID, 包括歌曲，歌单，专辑
     */
    @Schema(name = "中间ID, 包括歌曲，歌单，专辑")
    private Long middleId;
    
    
    /**
     * tag ID
     */
    @Schema(name = "tag ID")
    private Long tagId;
    
    
    /**
     * 0: 流派, 1: 歌曲tag, 2: 歌单tag, 3: mv标签
     */
    @Schema(name = "0: 流派, 1: 歌曲tag, 2: 歌单tag, 3: mv标签")
    private Integer type;
    
}
