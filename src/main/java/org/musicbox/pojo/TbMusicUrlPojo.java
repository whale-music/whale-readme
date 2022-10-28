package org.musicbox.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2022-10-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_music_url")
@ApiModel(value = "TbMusicUrlPojo对象", description = "音乐下载地址")
public class TbMusicUrlPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("音乐ID")
    @TableField("music_id")
    private Long musicId;

    @ApiModelProperty("音乐地址")
    @TableField("url")
    private String url;

    @ApiModelProperty("比特率，音频文件的信息")
    @TableField("rate")
    private Integer rate;

    @ApiModelProperty("音乐质量(sq: 无损，l：低质量，m：中质量，h：高质量，a：未知)")
    @TableField("quality")
    private String quality;

    @ApiModelProperty("保存音乐本体的md5，当上传新的音乐时做比较。如果相同则表示已存在")
    @TableField("md5")
    private Long md5;

    @ApiModelProperty("修改时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("文件格式类型")
    @TableField("encodeType")
    private byte[] encodeType;


}
