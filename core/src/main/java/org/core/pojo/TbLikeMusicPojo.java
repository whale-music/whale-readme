package org.core.pojo;

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

/**
 * <p>
 * 喜爱歌单中间表
 * </p>
 *
 * @author Sakura
 * @since 2022-12-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_like_music")
@ApiModel(value = "TbLikeMusicPojo对象", description = "喜爱歌单中间表")
public class TbLikeMusicPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("喜爱歌单ID")
    @TableId(value = "like_id", type = IdType.ASSIGN_ID)
    private Long likeId;

    @ApiModelProperty("音乐ID")
    @TableField("music_id")
    private Long musicId;


}
