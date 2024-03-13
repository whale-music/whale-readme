package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "自定义查询 用户收藏歌单")
public class TbUserCollectEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 用户ID
     */
    @Schema(name = "用户ID")
    private Long userId;
    
    
    /**
     * 歌单ID
     */
    @Schema(name = "歌单ID")
    private Long collectId;
    
}
