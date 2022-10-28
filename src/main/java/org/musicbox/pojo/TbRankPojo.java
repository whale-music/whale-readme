package org.musicbox.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2022-10-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_rank")
@ApiModel(value = "TbRankPojo对象", description = "音乐播放排行榜")
public class TbRankPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @ApiModelProperty("音乐ID")
    @TableField("music_id")
    private Long musicId;

    @ApiModelProperty("歌曲播放次数")
    @TableField("broadcast_count")
    private Integer broadcastCount;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("创建时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


}
