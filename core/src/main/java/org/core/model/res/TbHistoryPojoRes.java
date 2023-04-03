package org.core.model.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@ApiModel("保存 音乐播放历史(包括歌单，音乐，专辑）")
public class TbHistoryPojoRes implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 歌曲ID
     */
    @NotNull(message = "musicId can not null")
    @ApiModelProperty("歌曲ID")
    private Long musicId;
    
    
    /**
     * 听歌次数
     */
    @ApiModelProperty("听歌次数")
    private Integer count;
    
    
    /**
     * 历史类型
     */
    @ApiModelProperty("历史类型")
    private Integer type;
    
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
    
}
