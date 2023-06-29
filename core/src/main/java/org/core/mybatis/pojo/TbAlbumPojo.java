package org.core.mybatis.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 歌曲专辑表(TbAlbum)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:30
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_album")
@Schema(name = "TbAlbum", description = "歌曲专辑表")
public class TbAlbumPojo extends Model<TbAlbumPojo> implements Serializable {
    public static final long serialVersionUID = -51881178126349090L;
    
    @Schema(title = "专辑表ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "专辑名")
    @TableField("album_name")
    private String albumName;
    
    @Schema(title = "专辑版本（比如录音室版，现场版）")
    @TableField("sub_type")
    private String subType;
    
    @Schema(title = "专辑简介")
    @TableField("description")
    private String description;
    
    @Schema(title = "发行公司")
    @TableField("company")
    private String company;
    
    @Schema(title = "专辑发布时间")
    @TableField("publish_time")
    private LocalDateTime publishTime;
    
    @Schema(title = "上传用户ID")
    @TableField("user_id")
    private Long userId;
    
    @Schema(title = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @Schema(title = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    
}

