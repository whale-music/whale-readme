package org.core.jpa.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@ApiModel("保存 所有音乐列表")
public class TbMusicEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 音乐ID
     */
    @NotNull(message = "id can not null")
    @ApiModelProperty("音乐ID")
    private Long id;
    
    
    /**
     * 音乐名
     */
    @ApiModelProperty("音乐名")
    private String musicName;
    
    
    /**
     * 歌曲别名，数组则使用逗号分割
     */
    @ApiModelProperty("歌曲别名，数组则使用逗号分割")
    private String aliasName;
    
    
    /**
     * 专辑ID
     */
    @ApiModelProperty("专辑ID")
    private Long albumId;
    
    
    /**
     * 排序字段
     */
    @NotNull(message = "sort can not null")
    @ApiModelProperty("排序字段")
    private Long sort;
    
    
    /**
     * 上传用户ID
     */
    @ApiModelProperty("上传用户ID")
    private Long userId;
    
    
    /**
     * 歌曲时长
     */
    @ApiModelProperty("歌曲时长")
    private Integer timeLength;
    
    
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
}
