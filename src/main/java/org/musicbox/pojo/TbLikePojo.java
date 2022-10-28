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
 * 喜爱歌单
 * </p>
 *
 * @author Sakura
 * @since 2022-10-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_like")
@ApiModel(value = "TbLikePojo对象", description = "喜爱歌单")
public class TbLikePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("我喜欢的歌单ID和用户ID相同")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @ApiModelProperty("歌单名")
    @TableField("song_name")
    private String songName;

    @ApiModelProperty("封面地址")
    @TableField("pic")
    private String pic;

    @ApiModelProperty("简介")
    @TableField("description")
    private String description;

    @ApiModelProperty("歌单标签，表示歌单风格。使用字典表")
    @TableField("tag")
    private Long tag;

    @ApiModelProperty("修改时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;


}
