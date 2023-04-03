package org.core.model.res;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("自定义查询 歌单和音乐的中间表，用于记录歌单中的每一个音乐")
public class TbCollectMusicPojoQueryRes implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 歌单ID
     */
    @ApiModelProperty("歌单ID")
    private Long collectId;
    
    
    /**
     * 音乐ID
     */
    @ApiModelProperty("音乐ID")
    private Long musicId;
    
}
