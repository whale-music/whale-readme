package org.core.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
 * @since 2022-12-07
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("tb_artist")
@ApiModel(value = "TbArtistPojo对象", description = "歌手表")
public class TbArtistPojo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("歌手ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @ApiModelProperty("歌手名")
    @TableField("artist_name")
    private String artistName;
    
    @ApiModelProperty("歌手名")
    @TableField("alias_name")
    private String aliasName;

    @ApiModelProperty("歌手性别")
    @TableField("sex")
    private String sex;

    @ApiModelProperty("出生年月")
    @TableField("birth")
    private LocalDate birth;
    
    @ApiModelProperty("所在国家")
    @TableField("location")
    private String location;
    
    @ApiModelProperty("歌手介绍")
    @TableField("introduction")
    private String introduction;
    
    @ApiModelProperty("上传用户ID")
    @TableField("user_id")
    private Long userId;
    
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @ApiModelProperty("修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
