package org.core.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 歌手表
 * </p>
 *
 * @author Sakura
 * @since 2022-12-02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_singer")
@ApiModel(value = "TbSingerPojo对象", description = "歌手表")
public class TbSingerPojo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("歌手ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @ApiModelProperty("歌手名")
    @TableField("singer_name")
    private String singerName;
    
    @ApiModelProperty("歌手性别")
    @TableField("sex")
    private String sex;
    
    @ApiModelProperty("封面")
    @TableField("pic")
    private String pic;
    
    @ApiModelProperty("出生年月")
    @TableField("birth")
    private LocalDate birth;
    
    @ApiModelProperty("所在国家")
    @TableField("location")
    private String location;
    
    @ApiModelProperty("歌手介绍")
    @TableField("introduction")
    private String introduction;
    
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @ApiModelProperty("修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    
}
