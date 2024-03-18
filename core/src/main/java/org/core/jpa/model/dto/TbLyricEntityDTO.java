package org.core.jpa.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(title = "歌词表")
public class TbLyricEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键
     */
    @Schema(name = "主键")
    private Long id;
    
    
    /**
     * 音乐ID
     */
    @Schema(name = "音乐ID")
    private Long musicId;
    
    
    /**
     * 歌词类型
     */
    @Schema(name = "歌词类型")
    private String type;
    
    
    /**
     * 歌词
     */
    @Schema(name = "歌词")
    private String lyric;
    
    
    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 修改时间
     */
    @Schema(name = "修改时间")
    private LocalDateTime updateTime;
    
}
