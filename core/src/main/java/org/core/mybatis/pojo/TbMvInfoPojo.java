package org.core.mybatis.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 音乐短片(TbMv)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:31
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_mv_info")
@Schema(name = "TbMvInfo", description = "音乐短片信息")
public class TbMvInfoPojo extends Model<TbMvInfoPojo> implements Serializable {
    public static final long serialVersionUID = 839599165219313300L;
    
    @Schema(title = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "标题")
    @TableField("title")
    private String title;
    
    @Schema(title = "视频内容简介")
    @TableField("description")
    private String description;
    
    @Schema(title = "发布时间")
    @TableField("publish_time")
    private LocalDateTime publishTime;
    
    @Schema(title = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @Schema(title = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    
}

