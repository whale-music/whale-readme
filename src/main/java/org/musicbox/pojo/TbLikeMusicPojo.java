package org.musicbox.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 喜爱歌单中间表
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_like_music")
public class TbLikeMusicPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 喜爱歌单ID
     */
    @TableField("like_id")
    private Long likeId;

    /**
     * 音乐ID
     */
    @TableField("music_id")
    private Long musicId;


}
