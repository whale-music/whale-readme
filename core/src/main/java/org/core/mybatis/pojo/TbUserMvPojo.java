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
 * 用户收藏mv(TbUserMv)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:32
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user_mv")
@Schema(name = "TbUserMv", description = "用户收藏mv")
public class TbUserMvPojo extends Model<TbUserMvPojo> implements Serializable {
    public static final long serialVersionUID = -70732306746588306L;
    
    @Schema(title = "用户ID")
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;
    
    @Schema(title = "MV ID")
    @TableField("mv_id")
    private Long mvId;
    
    
}

