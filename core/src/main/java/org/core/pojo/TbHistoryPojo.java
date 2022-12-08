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
 * 音乐播放历史(包括歌单，音乐，专辑）
 * </p>
 *
 * @author Sakura
 * @since 2022-12-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_history")
@ApiModel(value = "TbHistoryPojo对象", description = "音乐播放历史(包括歌单，音乐，专辑）")
public class TbHistoryPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("歌曲ID")
    @TableId(value = "music_id", type = IdType.ASSIGN_ID)
    private Long musicId;

    @ApiModelProperty("听歌次数")
    @TableField("count")
    private Integer count;

    @ApiModelProperty("历史类型")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
