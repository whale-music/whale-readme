package org.core.jpa.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("歌单列表")
public class TbCollectEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 歌单表ID
     */
    @ApiModelProperty("歌单表ID")
    private Long id;
    
    
    /**
     * 歌单名（包括用户喜爱歌单）
     */
    @ApiModelProperty("歌单名（包括用户喜爱歌单）")
    private String playListName;
    
    
    /**
     * 歌单类型，0为普通歌单，1为用户喜爱歌单，2为推荐歌单
     */
    @ApiModelProperty("歌单类型，0为普通歌单，1为用户喜爱歌单，2为推荐歌单")
    private Integer type;
    
    
    /**
     * 简介
     */
    @ApiModelProperty("简介")
    private String description;
    
    
    /**
     * 创建人ID
     */
    @ApiModelProperty("创建人ID")
    private Long userId;
    
    
    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    private Long sort;
    
    
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
