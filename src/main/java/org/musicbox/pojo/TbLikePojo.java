package org.musicbox.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 喜爱歌单
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_like")
public class TbLikePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 喜爱歌单ID
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
     * 歌单标签，表示歌单风格。使用字典表
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
     * 创建用户ID
     */
    @TableField("user_id")
    private Long userId;


}
