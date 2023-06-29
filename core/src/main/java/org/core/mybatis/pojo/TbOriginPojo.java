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
 * 音乐来源(TbOrigin)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:31
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_origin")
@Schema(name = "TbOrigin", description = "音乐来源")
public class TbOriginPojo extends Model<TbOriginPojo> implements Serializable {
    public static final long serialVersionUID = -96641069693348305L;
    
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "音乐ID")
    @TableField("music_id")
    private Long musicId;
    
    @Schema(title = "源地址ID")
    @TableField("resource_id")
    private Long resourceId;
    
    @Schema(title = "来源")
    @TableField("origin")
    private String origin;
    
    @Schema(title = "来源地址")
    @TableField("origin_url")
    private String originUrl;
    
    
}

