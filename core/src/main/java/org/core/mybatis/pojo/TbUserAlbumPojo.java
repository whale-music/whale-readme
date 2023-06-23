package org.core.mybatis.pojo;

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
 * 用户收藏专辑表
 * </p>
 *
 * @author Sakura
 * @since 2023-03-14
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("tb_user_album")
@ApiModel(value = "TbUserAlbumPojo对象", description = "用户收藏专辑表")
public class TbUserAlbumPojo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("用户ID")
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;
    
    @ApiModelProperty("专辑ID")
    @TableField("album_id")
    private Long albumId;
    
    
}
