package org.core.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 * 所有音乐列表
 * </p>
 *
 * @author Sakura
 * @since 2022-10-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_music")
@ApiModel(value = "TbMusicPojo对象", description = "所有音乐列表")
public class TbMusicPojo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("所有音乐列表ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @ApiModelProperty("音乐名")
    @TableField("music_name")
    private String musicName;
    
    @ApiModelProperty("歌曲别名，数组则使用逗号分割")
    @TableField("alia_name")
    private String aliaName;
    
    @ApiModelProperty("歌曲封面地址")
    @TableField("pic")
    private String pic;
    
    @ApiModelProperty("歌手信息，id是歌手和歌曲的中间表")
    @TableField("singer_id")
    private Long singerId;
    
    @ApiModelProperty("歌词")
    @TableField("lyric")
    private String lyric;
    
    @ApiModelProperty("专辑ID")
    @TableField("album_id")
    private Long albumId;
    
    @ApiModelProperty("歌曲时长")
    @TableField("time_length")
    private LocalTime timeLength;
    
    @ApiModelProperty("排序字段")
    @TableField("sort")
    private Long sort;
    
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    
}
