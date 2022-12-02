package org.core.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 歌单列表
 * </p>
 *
 * @author Sakura
 * @since 2022-12-02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_collect")
@ApiModel(value = "TbCollectPojo对象", description = "歌单列表")
public class TbCollectPojo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("歌单表ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @ApiModelProperty("歌单名")
    @TableField("play_list_name")
    private String playListName;
    
    @ApiModelProperty("封面地址")
    @TableField("pic")
    private String pic;
    
    @ApiModelProperty("简介")
    @TableField("description")
    private String description;
    
    @ApiModelProperty("创建人ID")
    @TableField("user_id")
    private Long userId;
    
    @ApiModelProperty("排序字段")
    @TableField("sort")
    private Long sort;
    
    @ApiModelProperty("该歌单是否订阅(收藏). 0: 为创建,1: 为订阅(收藏)")
    @TableField("subscribed")
    private Boolean subscribed;
    
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @ApiModelProperty("修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
