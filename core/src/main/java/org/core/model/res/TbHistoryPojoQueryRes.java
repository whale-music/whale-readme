package org.core.model.res;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("自定义查询 音乐播放历史(包括歌单，音乐，专辑）")
public class TbHistoryPojoQueryRes implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 歌曲ID
     */
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
