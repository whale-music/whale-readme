package org.core.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 所有音乐列表
 * </p>
 *
 * @author Sakura
 * @since 2022-12-07
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("tb_music")
@ApiModel(value = "TbMusicPojo对象", description = "所有音乐列表")
public class TbMusicPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("音乐ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("音乐名")
    @TableField("music_name")
    private String musicName;

    @ApiModelProperty("歌曲别名，数组则使用逗号分割")
    @TableField("alias_name")
    private String aliasName;

    @ApiModelProperty("专辑ID")
    @TableField("album_id")
    private Long albumId;

    @ApiModelProperty("排序字段")
    @TableField("sort")
    private Long sort;

    @ApiModelProperty("歌曲时长")
    @TableField("time_length")
    private Integer timeLength;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}
