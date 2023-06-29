package org.core.mybatis.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 歌手表(TbArtist)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:31
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_artist")
@Schema(name = "TbArtist", description = "歌手表")
public class TbArtistPojo extends Model<TbArtistPojo> implements Serializable {
    public static final long serialVersionUID = 808510247656670263L;
    
    @Schema(title = "歌手ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "歌手名")
    @TableField("artist_name")
    private String artistName;
    
    @Schema(title = "歌手别名")
    @TableField("alias_name")
    private String aliasName;
    
    @Schema(title = "歌手性别")
    @TableField("sex")
    private String sex;
    
    @Schema(title = "出生年月")
    @TableField("birth")
    private LocalDate birth;
    
    @Schema(title = "所在国家")
    @TableField("location")
    private String location;
    
    @Schema(title = "歌手介绍")
    @TableField("introduction")
    private String introduction;
    
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

