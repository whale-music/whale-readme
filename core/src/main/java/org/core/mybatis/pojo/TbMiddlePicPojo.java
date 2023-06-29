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
 * 封面中间表(TbMiddlePic)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:31
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_middle_pic")
@Schema(name = "TbMiddlePic", description = "封面中间表")
public class TbMiddlePicPojo extends Model<TbMiddlePicPojo> implements Serializable {
    public static final long serialVersionUID = 743464341475997524L;
    
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "中间表")
    @TableField("middle_id")
    private Long middleId;
    
    @Schema(title = "封面ID")
    @TableField("pic_id")
    private Long picId;
    
    @Schema(title = "封面类型,歌单-1,专辑-2,歌手-3,歌手-3.用户头像-4,用户背景-5, mv标签-6")
    @TableField("type")
    private Byte type;
    
    
}

