package org.core.jpa.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@ApiModel("保存 歌单风格中间表")
public class TbMiddleTagEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 中间ID, 包括歌曲，歌单，专辑
     */
    @NotNull(message = "id can not null")
    @ApiModelProperty("中间ID, 包括歌曲，歌单，专辑")
    private Long id;
    
    
    /**
     * 中间键
     */
    @NotNull(message = "middleId can not null")
    @ApiModelProperty("中间键")
    private Long middleId;
    
    
    /**
     * tag ID
     */
    @NotNull(message = "tagId can not null")
    @ApiModelProperty("tag ID")
    private Long tagId;
    
    
    /**
     * 0流派, 1歌曲tag, 2歌单tag
     */
    @NotNull(message = "type can not null")
    @ApiModelProperty("0流派, 1歌曲tag, 2歌单tag")
    private Integer type;
    
}
