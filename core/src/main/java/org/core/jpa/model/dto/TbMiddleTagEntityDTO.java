package org.core.jpa.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("歌单风格中间表")
public class TbMiddleTagEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 中间ID, 包括歌曲，歌单，专辑
     */
    @ApiModelProperty("中间ID, 包括歌曲，歌单，专辑")
    private Long id;
    
    
    /**
     * 中间键
     */
    @ApiModelProperty("中间键")
    private Long middleId;
    
    
    /**
     * tag ID
     */
    @ApiModelProperty("tag ID")
    private Long tagId;
    
    
    /**
     * 0流派, 1歌曲tag, 2歌单tag
     */
    @ApiModelProperty("0流派, 1歌曲tag, 2歌单tag")
    private Integer type;
    
}
