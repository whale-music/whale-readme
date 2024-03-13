package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Schema(title = "保存 所有音乐列表")
public class TbMusicEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 音乐ID
     */
    @NotNull(message = "id can not null")
    @Schema(name = "音乐ID")
    private Long id;
    
    
    /**
     * 音乐名
     */
    @Schema(name = "音乐名")
    private String musicName;
    
    
    /**
     * 歌曲别名，数组则使用逗号分割
     */
    @Schema(name = "歌曲别名，数组则使用逗号分割")
    private String aliasName;
    
    
    /**
     * 专辑ID
     */
    @Schema(name = "专辑ID")
    private Long albumId;
    
    
    /**
     * 排序字段
     */
    @NotNull(message = "sort can not null")
    @Schema(name = "排序字段")
    private Long sort;
    
    
    /**
     * 上传用户ID
     */
    @Schema(name = "上传用户ID")
    private Long userId;
    
    
    /**
     * 歌曲时长
     */
    @Schema(name = "歌曲时长")
    private Integer timeLength;
    
    
    /**
     * 更新时间
     */
    @Schema(name = "更新时间")
    private LocalDateTime updateTime;
    
    
    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
    
}
