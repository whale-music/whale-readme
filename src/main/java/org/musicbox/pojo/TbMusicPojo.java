package org.musicbox.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 * 所有音乐列表
 * </p>
 *
 * @author Sakura
 * @since 2022-10-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_music")
public class TbMusicPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所有音乐列表ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 音乐名
     */
    @TableField("music_name")
    private String musicName;

    /**
     * 歌曲别名，数组则使用逗号分割
     */
    @TableField("alia_name")
    private String aliaName;

    /**
     * 歌手信息，id是歌手和歌曲的中间表
     */
    @TableField("singer_id")
    private Long singerId;

    /**
     * 歌词
     */
    @TableField("lyric")
    private String lyric;

    /**
     * 歌曲时长
     */
    @TableField("time_length")
    private LocalTime timeLength;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 专辑ID
     */
    @TableField("album_id")
    private Long albumId;


}
