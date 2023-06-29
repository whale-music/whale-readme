package org.core.mybatis.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

/**
 * 音乐与歌手中间表(TbMusicArtist)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:31
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_music_artist")
@Schema(name = "TbMusicArtist", description = "音乐与歌手中间表")
public class TbMusicArtistPojo extends Model<TbMusicArtistPojo> implements Serializable {
    public static final long serialVersionUID = 305069432394845773L;
    
    @Schema(title = "音乐ID")
    @TableId(value = "music_id", type = IdType.ASSIGN_ID)
    private Long musicId;
    
    @Schema(title = "艺术家ID")
    @TableField("artist_id")
    private Long artistId;
    
    
}

