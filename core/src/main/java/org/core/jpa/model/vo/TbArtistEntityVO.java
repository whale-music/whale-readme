package org.core.jpa.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@ApiModel("保存 歌手表")
public class TbArtistEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 歌手ID
     */
    @NotNull(message = "id can not null")
    @ApiModelProperty("歌手ID")
    private Long id;
    
    
    /**
     * 歌手名
     */
    @NotNull(message = "artistName can not null")
    @ApiModelProperty("歌手名")
    private String artistName;
    
    
    /**
     * 歌手别名
     */
    @ApiModelProperty("歌手别名")
    private String aliasName;
    
    
    /**
     * 歌手性别
     */
    @ApiModelProperty("歌手性别")
    private String sex;
    
    
    /**
     * 出生年月
     */
    @ApiModelProperty("出生年月")
    private LocalDate birth;
    
    
    /**
     * 所在国家
     */
    @ApiModelProperty("所在国家")
    private String location;
    
    
    /**
     * 歌手介绍
     */
    @ApiModelProperty("歌手介绍")
    private String introduction;
    
    
    /**
     * 上传用户ID
     */
    @ApiModelProperty("上传用户ID")
    private Long userId;
    
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
    
}
