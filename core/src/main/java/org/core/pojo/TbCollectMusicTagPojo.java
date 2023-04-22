package org.core.pojo;

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
 * @since 2022-12-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_collect_music_tag")
@ApiModel(value = "TbCollectTagPojo对象", description = "歌单风格中间表")
public class TbCollectMusicTagPojo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("中间ID, 包括歌曲，歌单")
    @TableId(value = "id", type = IdType.NONE)
    private Long id;
    
    @ApiModelProperty("tag ID")
    @TableField("tag_id")
    private Long tagId;
    
    @ApiModelProperty("0流派, 1歌曲tag, 2歌单tag")
    @TableField("type")
    private Short type;
    
}
