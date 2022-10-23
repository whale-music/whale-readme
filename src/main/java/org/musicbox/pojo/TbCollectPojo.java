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
 * 歌单列表
 * </p>
 *
 * @author Sakura
 * @since 2022-10-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_collect")
public class TbCollectPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 歌单表ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 歌单名
     */
    @TableField("song_name")
    private String songName;

    /**
     * 封面地址
     */
    @TableField("pic")
    private String pic;

    /**
     * 简介
     */
    @TableField("introduce")
    private String introduce;

    /**
     * 歌单标签，表示歌单风格。保存标签到标签表，逗号分割
     */
    @TableField("style")
    private String style;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人ID
     */
    @TableField("user_id")
    private Long userId;


}
