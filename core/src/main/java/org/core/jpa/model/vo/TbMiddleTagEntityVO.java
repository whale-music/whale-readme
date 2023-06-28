package org.core.jpa.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel("保存 歌单风格中间表")
public class TbMiddleTagEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "id can not null")
    private Long id;
    
    
    /**
     * 中间ID, 包括歌曲，歌单，专辑
     */
    @NotNull(message = "middleId can not null")
    @ApiModelProperty("中间ID, 包括歌曲，歌单，专辑")
    private Long middleId;
    
    
    /**
     * tag ID
     */
    @NotNull(message = "tagId can not null")
    @ApiModelProperty("tag ID")
    private Long tagId;
    
    
    /**
     * 0: 流派, 1: 歌曲tag, 2: 歌单tag, 3: mv标签
     */
    @NotNull(message = "type can not null")
    @ApiModelProperty("0: 流派, 1: 歌曲tag, 2: 歌单tag, 3: mv标签")
    private Integer type;
    
}
