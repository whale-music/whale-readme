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
 * 歌单个风格中间表（标签）
 * </p>
 *
 * @author Sakura
 * @since 2022-10-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_collect_style")
public class TbCollectStylePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("collect_id")
    private Long collectId;

    @TableField("style_id")
    private Long styleId;


}
