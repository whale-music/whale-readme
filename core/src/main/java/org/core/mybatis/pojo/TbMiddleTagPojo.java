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
 * 歌单风格中间表(TbMiddleTag)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:31
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_middle_tag")
@Schema(name = "TbMiddleTag", description = "歌单风格中间表")
public class TbMiddleTagPojo extends Model<TbMiddleTagPojo> implements Serializable {
    public static final long serialVersionUID = 854537454685420670L;
    
    @Schema(title = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "中间ID, 包括歌曲，歌单，专辑")
    @TableField("middle_id")
    private Long middleId;
    
    @Schema(title = "tag ID")
    @TableField("tag_id")
    private Long tagId;
    
    @Schema(title = "0: 流派, 1: 歌曲tag, 2: 歌单tag, 3: mv标签")
    @TableField("type")
    private Byte type;
    
    
}

