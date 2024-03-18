package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;


@Data
@Schema(title = "保存 歌单和音乐的中间表，用于记录歌单中的每一个音乐")
public class TbCollectMusicEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 歌单ID
     */
    @NotNull(message = "collectId can not null")
    @Schema(name = "歌单ID")
    private Long collectId;
    
    
    /**
     * 音乐ID
     */
    @NotNull(message = "musicId can not null")
    @Schema(name = "音乐ID")
    private Long musicId;
    
    
    /**
     * 添加顺序
     */
    @NotNull(message = "sort can not null")
    @Schema(name = "添加顺序")
    private Long sort;
    
}
