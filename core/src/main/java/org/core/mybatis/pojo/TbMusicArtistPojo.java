package org.core.mybatis.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 音乐与歌手中间表
 * </p>
 *
 * @author Sakura
 * @since 2023-06-25
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_music_artist")
@ApiModel(value = "TbMusicArtistPojo对象", description = "音乐与歌手中间表")
public class TbMusicArtistPojo extends Model<TbMusicArtistPojo> implements Serializable{

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("音乐ID")
    @TableId("music_id")
    private Long musicId;
    
    @ApiModelProperty("艺术家ID")
    @TableField("artist_id")
    private Long artistId;
    
    
}
