package org.core.jpa.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("歌词表")
public class TbLyricEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;
    
    
    /**
     * 音乐ID
     */
    @ApiModelProperty("音乐ID")
    private Long musicId;
    
    
    /**
     * 歌词类型
     */
    @ApiModelProperty("歌词类型")
    private String type;
    
    
    /**
     * 歌词
     */
    @ApiModelProperty("歌词")
    private String lyric;
    
    
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
