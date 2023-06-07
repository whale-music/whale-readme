package org.core.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 歌单风格中间表
 * </p>
 *
 * @author Sakura
 * @since 2022-12-07
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("tb_middle_tag")
@ApiModel(value = "TbCollectTagPojo对象", description = "歌单风格中间表")
public class TbMiddleTagPojo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @ApiModelProperty("中间ID, 包括歌曲，歌单，专辑")
    @TableField(value = "middle_id")
    private Long middleId;
    
    @ApiModelProperty("tag ID")
    @TableField("tag_id")
    private Long tagId;
    
    @ApiModelProperty("0流派, 1歌曲tag, 2歌单tag")
    @TableField("type")
    private Short type;
    
}
