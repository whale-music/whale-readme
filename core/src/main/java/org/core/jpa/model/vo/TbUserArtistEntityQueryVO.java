package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "自定义查询 用户关注歌曲家")
public class TbUserArtistEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 用户ID
     */
    @Schema(name = "用户ID")
    private Long userId;
    
    
    /**
     * 歌手ID
     */
    @Schema(name = "歌手ID")
    private Long artistId;
    
}
