package org.musicbox.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 歌曲专辑表
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_album")
public class TbAlbumPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 专辑表ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 专辑名
     */
    @TableField("album_name")
    private String albumName;

    /**
     * 专辑封面地址
     */
    @TableField("pic")
    private String pic;

    /**
     * 专辑简介
     */
    @TableField("indirect")
    private String indirect;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDate updateTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDate createTime;


}
