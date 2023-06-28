package org.core.jpa.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("音乐播放历史(包括歌单，音乐，专辑）")
public class TbHistoryEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    
    
    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;
    
    
    /**
     * 播放ID，可能是歌曲，专辑，歌单，mv
     */
    @ApiModelProperty("播放ID，可能是歌曲，专辑，歌单，mv")
    private Long middleId;
    
    
    /**
     * 播放类型可能是音乐，歌单，专辑,0为音乐，1为歌单，2为专辑, 3mv
     */
    @ApiModelProperty("播放类型可能是音乐，歌单，专辑,0为音乐，1为歌单，2为专辑, 3mv")
    private Integer type;
    
    
    /**
     * 歌曲播放次数
     */
    @ApiModelProperty("歌曲播放次数")
    private Integer count;
    
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    
}
