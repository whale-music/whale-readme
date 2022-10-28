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

/**
 * <p>
 * 歌曲和歌手的中间表
 * </p>
 *
 * @author Sakura
 * @since 2022-10-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_music_singer")
@ApiModel(value = "TbMusicSingerPojo对象", description = "歌曲和歌手的中间表")
public class TbMusicSingerPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("歌曲ID")
    @TableId(value = "music_id", type = IdType.AUTO)
    private Long musicId;

    @ApiModelProperty("歌曲ID")
    @TableField(value = "singer_id")
    private Long singerId;


}
