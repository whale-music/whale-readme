package org.musicbox.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 歌手表
 * </p>
 *
 * @author Sakura
 * @since 2022-10-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_singer")
public class TbSingerPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 歌手ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 歌手名
     */
    @TableField("singer_name")
    private String singerName;

    /**
     * 歌手性别
     */
    @TableField("sex")
    private String sex;

    /**
     * 封面
     */
    @TableField("pic")
    private String pic;

    /**
     * 出生年月
     */
    @TableField("birth")
    private LocalDate birth;

    /**
     * 所在国家
     */
    @TableField("location")
    private String location;
    
    /**
     * 歌手介绍
     */
    @TableField("introduction")
    private String introduction;
    
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
}
