package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Schema(title = "保存 歌词表")
public class TbLyricEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 主键
     */
    @NotNull(message = "id can not null")
    @Schema(name = "主键")
    private Long id;
    
    
    /**
     * 音乐ID
     */
    @NotNull(message = "musicId can not null")
    @Schema(name = "音乐ID")
    private Long musicId;
    
    
    /**
     * 歌词类型
     */
    @NotNull(message = "type can not null")
    @Schema(name = "歌词类型")
    private String type;
    
    
    /**
     * 歌词
     */
    @NotNull(message = "lyric can not null")
    @Schema(name = "歌词")
    private String lyric;
    
    
    /**
     * 创建时间
     */
    @NotNull(message = "createTime can not null")
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 修改时间
     */
    @NotNull(message = "updateTime can not null")
    @Schema(name = "修改时间")
    private LocalDateTime updateTime;
    
}
