package org.core.mybatis.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 音乐专辑歌单封面表(TbPic)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:32
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_pic")
@Schema(name = "TbPic", description = "音乐专辑歌单封面表")
public class TbPicPojo extends Model<TbPicPojo> implements Serializable {
    public static final long serialVersionUID = 403546337911906819L;
    
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "音乐网络地址，或路径")
    @TableField("path")
    private String path;
    
    @TableField("md5")
    private String md5;
    
    @Schema(title = "图片关联数量")
    @TableField("count")
    private Integer count;
    
    @Schema(title = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @Schema(title = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    
}

