package org.core.mybatis.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 音乐播放历史(包括歌单，音乐，专辑）(TbHistory)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:31
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_history")
@Schema(name = "TbHistory", description = "音乐播放历史(包括歌单，音乐，专辑）")
public class TbHistoryPojo extends Model<TbHistoryPojo> implements Serializable {
    public static final long serialVersionUID = 384861106429711509L;
    
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "用户ID")
    @TableField("user_id")
    private Long userId;
    
    @Schema(title = "播放ID，可能是歌曲，专辑，歌单，mv")
    @TableField("middle_id")
    private Long middleId;
    
    @Schema(title = "播放类型可能是音乐，歌单，专辑,0为音乐，1为歌单，2为专辑, 3mv")
    @TableField("type")
    private Byte type;
    
    @Schema(title = "歌曲播放次数")
    @TableField("count")
    private Integer count;
    
    @Schema(title = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @Schema(title = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    
}

