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
 * 歌单风格中间表
 * </p>
 *
 * @author Sakura
 * @since 2022-10-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_collect_tag")
@ApiModel(value = "TbCollectTagPojo对象", description = "歌单风格中间表")
public class TbCollectTagPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("歌单ID")
    @TableId(value = "collect_id", type = IdType.AUTO)
    private Long collectId;

    @ApiModelProperty("tag ID")
    @TableField(value = "tag_id")
    private Long tagId;


}
