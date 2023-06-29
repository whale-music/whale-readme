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
 * 歌单和音乐的中间表，用于记录歌单中的每一个音乐(TbCollectMusic)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:31
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_collect_music")
@Schema(name = "TbCollectMusic", description = "歌单和音乐的中间表，用于记录歌单中的每一个音乐")
public class TbCollectMusicPojo extends Model<TbCollectMusicPojo> implements Serializable {
    public static final long serialVersionUID = 887945119772731986L;
    
    @Schema(title = "歌单ID")
    @TableId(value = "collect_id", type = IdType.ASSIGN_ID)
    private Long collectId;
    
    @Schema(title = "音乐ID")
    @TableField("music_id")
    private Long musicId;
    
    @Schema(title = "添加顺序")
    @TableField("sort")
    private Long sort;
    
    
}

