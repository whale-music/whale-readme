package org.core.model.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@ApiModel("保存 所有音乐列表")
public class TbMusicPojoRes implements Serializable {
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
     * 歌曲封面地址
     */
    @ApiModelProperty("歌曲封面地址")
    private String pic;
    
    
    /**
     * 歌词
     */
    @ApiModelProperty("歌词")
    private String lyric;
    
    
    /**
     * 逐字歌词
     */
    @ApiModelProperty("逐字歌词")
    private String kLyric;
    
    
    /**
     * 专辑ID
     */
    @ApiModelProperty("专辑ID")
    private Long albumId;
    
    
    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    private Long sort;
    
    
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
