package org.core.mybatis.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 存储地址(TbResource)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:32
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_resource")
@Schema(name = "TbResource", description = "存储地址")
public class TbResourcePojo extends Model<TbResourcePojo> implements Serializable {
    public static final long serialVersionUID = -99482690661339071L;
    
    @Schema(title = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "音乐ID")
    @TableField("music_id")
    private Long musicId;
    
    @Schema(title = "比特率，音频文件的信息")
    @TableField("rate")
    private Integer rate;
    
    @Schema(title = "音乐地址, 存储相对路径")
    @TableField("path")
    private String path;
    
    @Schema(title = "保存音乐本体的md5，当上传新的音乐时做比较。如果相同则表示已存在")
    @TableField("md5")
    private String md5;
    
    @Schema(title = "音乐质量")
    @TableField("level")
    private String level;
    
    @Schema(title = "文件格式类型")
    @TableField("encode_type")
    private String encodeType;
    
    @Schema(title = "文件大小")
    @TableField("size")
    private Long size;
    
    @Schema(title = "上传用户ID")
    @TableField("user_id")
    private Long userId;
    
    @Schema(title = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @Schema(title = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    
}

