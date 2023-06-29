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
 * 歌手和专辑中间表(TbAlbumArtist)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:30
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_album_artist")
@Schema(name = "TbAlbumArtist", description = "歌手和专辑中间表")
public class TbAlbumArtistPojo extends Model<TbAlbumArtistPojo> implements Serializable {
    public static final long serialVersionUID = 673650108114983114L;
    
    @Schema(title = "专辑ID")
    @TableId(value = "album_id", type = IdType.ASSIGN_ID)
    private Long albumId;
    
    @Schema(title = "歌手ID")
    @TableField("artist_id")
    private Long artistId;
    
    
}

