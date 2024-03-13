package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Schema(title = "保存 音乐播放历史(包括歌单，音乐，专辑）")
public class TbHistoryEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "id can not null")
    private Long id;
    
    
    /**
     * 用户ID
     */
    @NotNull(message = "userId can not null")
    @Schema(name = "用户ID")
    private Long userId;
    
    
    /**
     * 播放ID，可能是歌曲，专辑，歌单，mv
     */
    @NotNull(message = "middleId can not null")
    @Schema(name = "播放ID，可能是歌曲，专辑，歌单，mv")
    private Long middleId;
    
    
    /**
     * 播放类型可能是音乐，歌单，专辑,0为音乐，1为歌单，2为专辑, 3mv
     */
    @Schema(name = "播放类型可能是音乐，歌单，专辑,0为音乐，1为歌单，2为专辑, 3mv")
    private Integer type;
    
    
    /**
     * 歌曲播放次数
     */
    @Schema(name = "歌曲播放次数")
    private Integer count;
    
    
    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 更新时间
     */
    @Schema(name = "更新时间")
    private LocalDateTime updateTime;
    
}
