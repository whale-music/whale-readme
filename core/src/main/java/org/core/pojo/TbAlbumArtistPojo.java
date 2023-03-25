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
 *
 * </p>
 *
 * @author Sakura
 * @since 2022-12-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_album_singer")
@ApiModel(value = "TbAlbumArtistPojo对象", description = "歌手和专辑中间表")
public class TbAlbumArtistPojo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("专辑ID")
    @TableId(value = "album_id", type = IdType.ASSIGN_ID)
    private Long albumId;
    
    @ApiModelProperty("歌手ID")
    @TableField("artist_id")
    private Long artistId;


}
