package org.core.jpa.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Data
@ApiModel("保存 歌单列表")
public class TbCollectEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 歌单表ID
     */
    @NotNull(message = "id can not null")
    @ApiModelProperty("歌单表ID")
    private Long id;
    
    
    /**
     * 歌单名（包括用户喜爱歌单）
     */
    @NotNull(message = "playListName can not null")
    @ApiModelProperty("歌单名（包括用户喜爱歌单）")
    private String playListName;
    
    
    /**
     * 歌单类型，0为普通歌单，1为用户喜爱歌单，2为推荐歌单
     */
    @NotNull(message = "type can not null")
    @ApiModelProperty("歌单类型，0为普通歌单，1为用户喜爱歌单，2为推荐歌单")
    private Integer type;
    
    
    /**
     * 该歌单是否订阅(收藏). 0: 为创建,1: 为订阅(收藏)
     */
    @NotNull(message = "subscribed can not null")
    @ApiModelProperty("该歌单是否订阅(收藏). 0: 为创建,1: 为订阅(收藏)")
    private Integer subscribed;
    
    
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
    private Date createTime;
    
    
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;
    
}
