package org.core.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 音乐下载地址
 * </p>
 *
 * @author Sakura
 * @since 2022-12-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_music_url")
@ApiModel(value = "TbMusicUrlPojo对象", description = "音乐下载地址")
public class TbMusicUrlPojo implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("音乐ID")
    @TableField("music_id")
    private Long musicId;

    @ApiModelProperty("比特率，音频文件的信息")
    @TableField("rate")
    private Integer rate;

    @ApiModelProperty("音乐地址")
    @TableField("url")
    private String url;

    @ApiModelProperty("音乐质量(sq: 无损，l：低质量，m：中质量，h：高质量，a：未知)")
    @TableField("quality")
    private String quality;

    @ApiModelProperty("保存音乐本体的md5，当上传新的音乐时做比较。如果相同则表示已存在")
    @TableField("md5")
    private String md5;

    @ApiModelProperty("文件格式类型")
    @TableField("encodeType")
    private String encodeType;
    
    @ApiModelProperty("文件大小")
    @TableField("size")
    private Long size;
    
    @ApiModelProperty("上传用户ID")
    @TableField("user_id")
    private Long userId;
    
    @ApiModelProperty("音乐来源")
    @TableField("origin")
    private String origin;
    
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @ApiModelProperty("修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    
}
