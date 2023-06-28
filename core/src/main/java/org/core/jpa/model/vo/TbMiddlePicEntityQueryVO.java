package org.core.jpa.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("自定义查询 封面中间表")
public class TbMiddlePicEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    
    /**
     * 中间表
     */
    @ApiModelProperty("中间表")
    private Long middleId;
    
    
    /**
     * 封面ID
     */
    @ApiModelProperty("封面ID")
    private Long picId;
    
    
    /**
     * 封面类型,歌单-1,专辑-2,歌手-3,歌手-3.用户头像-4,用户背景-5, mv标签-6
     */
    @ApiModelProperty("封面类型,歌单-1,专辑-2,歌手-3,歌手-3.用户头像-4,用户背景-5, mv标签-6")
    private Integer type;
    
}
