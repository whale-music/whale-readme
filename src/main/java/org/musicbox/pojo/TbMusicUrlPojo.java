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

/**
 * <p>
 * 音乐下载地址
 * </p>
 *
 * @author Sakura
 * @since 2022-10-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_music_url")
public class TbMusicUrlPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 音乐ID
     */
    @TableField("music_id")
    private Long musicId;

    /**
     * 音乐地址
     */
    @TableField("url")
    private String url;

    /**
     * 比特率，音频文件的信息
     */
    @TableField("rate")
    private Integer rate;

    /**
     * 音乐质量(sq: 无损，l：低质量，m：中质量，h：高质量，a：未知)
     */
    @TableField("quality")
    private String quality;

    /**
     * 保存音乐本体的md5，当上传新的音乐时做比较。如果相同则表示已存在
     */
    @TableField("md5")
    private Long md5;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 文件格式类型
     */
    @TableField("encodeType")
    private byte[] encodeType;


}
