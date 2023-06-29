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
 * 用户收藏专辑表(TbUserAlbum)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:32
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user_album")
@Schema(name = "TbUserAlbum", description = "用户收藏专辑表")
public class TbUserAlbumPojo extends Model<TbUserAlbumPojo> implements Serializable {
    public static final long serialVersionUID = -57594223487025413L;
    
    @Schema(title = "用户ID")
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;
    
    @Schema(title = "专辑ID")
    @TableField("album_id")
    private Long albumId;
    
    
}

