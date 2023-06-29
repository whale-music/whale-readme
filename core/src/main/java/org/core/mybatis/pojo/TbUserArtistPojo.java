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
 * 用户关注歌曲家(TbUserArtist)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:32
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user_artist")
@Schema(name = "TbUserArtist", description = "用户关注歌曲家")
public class TbUserArtistPojo extends Model<TbUserArtistPojo> implements Serializable {
    public static final long serialVersionUID = 284443932366017667L;
    
    @Schema(title = "用户ID")
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;
    
    @Schema(title = "歌手ID")
    @TableField("artist_id")
    private Long artistId;
    
    
}

