package org.musicbox.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2022-10-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_music_singer")
public class TbMusicSingerPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 歌曲ID
     */
    @TableField("music_id")
    private Long musicId;

    /**
     * 歌曲ID
     */
    @TableField("singer_id")
    private Long singerId;


}
