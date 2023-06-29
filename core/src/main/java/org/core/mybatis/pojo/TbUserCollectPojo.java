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
 * 用户收藏歌单(TbUserCollect)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:32
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user_collect")
@Schema(name = "TbUserCollect", description = "用户收藏歌单")
public class TbUserCollectPojo extends Model<TbUserCollectPojo> implements Serializable {
    public static final long serialVersionUID = -46239280821283249L;
    
    @Schema(title = "用户ID")
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;
    
    @Schema(title = "歌单ID")
    @TableField("collect_id")
    private Long collectId;
    
    
}

