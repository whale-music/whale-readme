package org.core.model.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@ApiModel("保存 音乐播放排行榜")
public class TbRankPojoRes implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "id can not null")
    private Long id;
    
    
    /**
     * 用户ID
     */
    @NotNull(message = "userId can not null")
    @ApiModelProperty("用户ID")
    private Long userId;
    
    
    /**
     * 播放ID，可能是歌曲，专辑，歌单
     */
    @ApiModelProperty("播放ID，可能是歌曲，专辑，歌单")
    private Integer broadcastId;
    
    
    /**
     * 播放类型可能是音乐，歌单，专辑,0为音乐，1为歌单，2为专辑
     */
    @ApiModelProperty("播放类型可能是音乐，歌单，专辑,0为音乐，1为歌单，2为专辑")
    private Integer broadcastType;
    
    
    /**
     * 歌曲播放次数
     */
    @ApiModelProperty("歌曲播放次数")
    private Integer broadcastCount;
    
    
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
