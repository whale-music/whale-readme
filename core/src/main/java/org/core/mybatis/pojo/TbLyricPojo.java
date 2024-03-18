package org.core.mybatis.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 歌词表(TbLyric)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:31
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_lyric")
@Schema(title = "TbLyric", description = "歌词表")
public class TbLyricPojo extends Model<TbLyricPojo> implements Serializable {
    public static final long serialVersionUID = -11962312952892487L;
    
    @Schema(title = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "音乐ID")
    @TableField("music_id")
    private Long musicId;
    
    @Schema(title = "歌词类型")
    @TableField("type")
    private String type;
    
    @Schema(title = "歌词")
    @TableField("lyric")
    private String lyric;
    
    @Schema(title = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @Schema(title = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    
}

