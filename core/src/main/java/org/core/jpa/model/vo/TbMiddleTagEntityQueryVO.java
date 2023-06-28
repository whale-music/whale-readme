package org.core.jpa.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("自定义查询 歌单风格中间表")
public class TbMiddleTagEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    
    /**
     * 中间ID, 包括歌曲，歌单，专辑
     */
    @ApiModelProperty("中间ID, 包括歌曲，歌单，专辑")
    private Long middleId;
    
    
    /**
     * tag ID
     */
    @ApiModelProperty("tag ID")
    private Long tagId;
    
    
    /**
     * 0: 流派, 1: 歌曲tag, 2: 歌单tag, 3: mv标签
     */
    @ApiModelProperty("0: 流派, 1: 歌曲tag, 2: 歌单tag, 3: mv标签")
    private Integer type;
    
}
