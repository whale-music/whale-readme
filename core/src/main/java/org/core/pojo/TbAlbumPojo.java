package org.core.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 歌曲专辑表
 * </p>
 *
 * @author Sakura
 * @since 2022-12-07
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("tb_album")
@ApiModel(value = "TbAlbumPojo对象", description = "歌曲专辑表")
public class TbAlbumPojo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("专辑表ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @ApiModelProperty("专辑名")
    @TableField("album_name")
    private String albumName;
    
    @ApiModelProperty("专辑版本（比如录音室版，现场版）")
    @TableField("sub_type")
    private String subType;
    
    @ApiModelProperty("专辑简介")
    @TableField("description")
    private String description;
    
    @ApiModelProperty("发行公司")
    @TableField("company")
    private String company;
    
    @ApiModelProperty("专辑封面地址")
    @TableField("pic_id")
    private Long picId;
    
    @ApiModelProperty("专辑发布时间")
    @TableField("publish_time")
    private LocalDateTime publishTime;
    
    @ApiModelProperty("修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}
