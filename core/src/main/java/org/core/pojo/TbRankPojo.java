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
 * 音乐播放排行榜
 * </p>
 *
 * @author Sakura
 * @since 2022-12-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_rank")
@ApiModel(value = "TbRankPojo对象", description = "音乐播放排行榜")
public class TbRankPojo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @ApiModelProperty("用户ID")
    @TableField("user_id")
    private Long userId;
    
    @ApiModelProperty("播放ID，可能是歌曲，专辑，歌单")
    @TableField("broadcast_id")
    private Long broadcastId;
    
    @ApiModelProperty("播放类型可能是音乐，歌单，专辑,0为音乐，1为歌单，2为专辑")
    @TableField("broadcast_type")
    private Integer broadcastType;
    
    @ApiModelProperty("歌曲播放次数")
    @TableField("broadcast_count")
    private Integer broadcastCount;
    
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
