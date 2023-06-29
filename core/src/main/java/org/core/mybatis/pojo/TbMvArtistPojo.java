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
 * mv和歌手中间表(TbMvArtist)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:31
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_mv_artist")
@Schema(name = "TbMvArtist", description = "mv和歌手中间表")
public class TbMvArtistPojo extends Model<TbMvArtistPojo> implements Serializable {
    public static final long serialVersionUID = 851927785391348325L;
    
    @TableId(value = "mv_id", type = IdType.ASSIGN_ID)
    private Long mvId;
    
    @TableField("artist_id")
    private Long artistId;
    
    
}

