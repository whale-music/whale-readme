package org.core.jpa.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(title = "所有音乐列表")
public class TbMusicEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 音乐ID
     */
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
