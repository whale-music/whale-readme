package org.core.mybatis.pojo;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 所有音乐列表(TbMusic)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:31
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_music")
@Schema(name = "TbMusic", description = "所有音乐列表")
public class TbMusicPojo extends Model<TbMusicPojo> implements Serializable {
    public static final long serialVersionUID = -57031801034641798L;
    
    @Schema(title = "音乐ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "音乐名")
    @TableField("music_name")
    private String musicName;
    
    @Schema(title = "歌曲别名，数组则使用逗号分割")
    @TableField("alias_name")
    private String aliasName;
    
    @Schema(title = "专辑ID")
    @TableField("album_id")
    private Long albumId;
    
    @Schema(title = "上传用户ID")
    @TableField("user_id")
    private Long userId;
    
    @Schema(title = "歌曲时长")
    @TableField("time_length")
    private Integer timeLength;
    
    @Schema(title = "音乐描述")
    @TableField("comment")
    private String comment;
    
    @Schema(title = "音乐语种")
    @TableField("language")
    private String language;
    
    @Schema(title = "歌曲发布时间")
    @TableField("publish_time")
    private LocalDateTime publishTime;
    
    @Schema(title = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @Schema(title = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    
    public Long getPublishTimeToTime() {
        if (Objects.isNull(publishTime)) {
            return null;
        }
        return DateUtil.date(publishTime).getTime();
    }
    
}

