package org.core.model.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("歌曲专辑表")
public class TbAlbumPojoReq implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 专辑表ID
     */
    @ApiModelProperty("专辑表ID")
    private Long id;
    
    
    /**
     * 专辑名
     */
    @ApiModelProperty("专辑名")
    private String albumName;
    
    
    /**
     * 专辑版本（比如录音室版，现场版）
     */
    @ApiModelProperty("专辑版本（比如录音室版，现场版）")
    private String subType;
    
    
    /**
     * 专辑简介
     */
    @ApiModelProperty("专辑简介")
    private String description;
    
    
    /**
     * 发行公司
     */
    @ApiModelProperty("发行公司")
    private String company;
    
    
    /**
     * 专辑封面地址
     */
    @ApiModelProperty("专辑封面地址")
    private String pic;
    
    
    /**
     * 专辑发布时间
     */
    @ApiModelProperty("专辑发布时间")
    private LocalDateTime publishTime;
    
    
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
    
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
}
