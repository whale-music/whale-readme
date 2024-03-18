package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(title = "自定义查询 音乐短片")
public class TbMvEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    
    /**
     * 视频资源ID
     */
    @Schema(name = "视频资源ID")
    private Long sourceId;
    
    
    /**
     * 标题
     */
    @Schema(name = "标题")
    private String title;
    
    
    /**
     * 视频内容简介
     */
    @Schema(name = "视频内容简介")
    private String description;
    
    
    /**
     * 视频时长
     */
    @Schema(name = "视频时长")
    private Integer duration;
    
    
    /**
     * 上传用户
     */
    @Schema(name = "上传用户")
    private Long userId;
    
    
    /**
     * 发布时间
     */
    @Schema(name = "发布时间")
    private LocalDateTime publishTime;
    
    
    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 更新时间
     */
    @Schema(name = "更新时间")
    private LocalDateTime updateTime;
    
}
