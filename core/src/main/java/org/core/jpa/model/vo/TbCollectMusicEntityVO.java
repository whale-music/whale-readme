package org.core.jpa.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@ApiModel("保存 歌单和音乐的中间表，用于记录歌单中的每一个音乐")
public class TbCollectMusicEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 歌单ID
     */
    @NotNull(message = "collectId can not null")
    @ApiModelProperty("歌单ID")
    private Long collectId;
    
    
    /**
     * 音乐ID
     */
    @NotNull(message = "musicId can not null")
    @ApiModelProperty("音乐ID")
    private Long musicId;
    
    
    /**
     * 添加顺序
     */
    @NotNull(message = "sort can not null")
    @ApiModelProperty("添加顺序")
    private Long sort;
    
}
