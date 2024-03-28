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
 * 歌单列表(TbCollect)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:31
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_collect")
@Schema(name = "TbCollect", description = "歌单列表")
public class TbCollectPojo extends Model<TbCollectPojo> implements Serializable {
    public static final long serialVersionUID = 383177139823756273L;
    
    @Schema(title = "歌单表ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "歌单名（包括用户喜爱歌单）")
    @TableField("play_list_name")
    private String playListName;
    
    @Schema(title = "歌单类型，0为普通歌单，1为用户喜爱歌单，2为推荐歌单")
    @TableField("type")
    private Byte type;
    
    @Schema(title = "简介")
    @TableField("description")
    private String description;
    
    @Schema(title = "创建人ID")
    @TableField("user_id")
    private Long userId;
    
    @Schema(title = "排序字段")
    @TableField("sort")
    private Long sort;
    
    @Schema(title = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @Schema(title = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    public Long getCreateTimeToTime() {
        if (Objects.isNull(this.createTime)) {
            return null;
        }
        return DateUtil.date(createTime).getTime();
    }
    
    public Long getUpdateTimeToTime() {
        if (Objects.isNull(this.updateTime)) {
            return null;
        }
        return DateUtil.date(updateTime).getTime();
    }
}

