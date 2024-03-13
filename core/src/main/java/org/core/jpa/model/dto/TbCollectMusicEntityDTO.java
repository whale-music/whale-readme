package org.core.jpa.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "歌单和音乐的中间表，用于记录歌单中的每一个音乐")
public class TbCollectMusicEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 歌单ID
     */
    @Schema(name = "歌单ID")
    private Long collectId;
    
    
    /**
     * 音乐ID
     */
    @Schema(name = "音乐ID")
    private Long musicId;
    
    
    /**
     * 添加顺序
     */
    @Schema(name = "添加顺序")
    private Long sort;
    
}
