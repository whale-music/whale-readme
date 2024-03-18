package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(title = "自定义查询 歌曲专辑表")
public class TbAlbumEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 专辑表ID
     */
    @Schema(name = "专辑表ID")
    private Long id;
    
    
    /**
     * 专辑名
     */
    @Schema(name = "专辑名")
    private String albumName;
    
    
    /**
     * 专辑版本（比如录音室版，现场版）
     */
    @Schema(name = "专辑版本（比如录音室版，现场版）")
    private String subType;
    
    
    /**
     * 专辑简介
     */
    @Schema(name = "专辑简介")
    private String description;
    
    
    /**
     * 发行公司
     */
    @Schema(name = "发行公司")
    private String company;
    
    
    /**
     * 专辑发布时间
     */
    @Schema(name = "专辑发布时间")
    private LocalDateTime publishTime;
    
    
    /**
     * 上传用户ID
     */
    @Schema(name = "上传用户ID")
    private Long userId;
    
    
    /**
     * 修改时间
     */
    @Schema(name = "修改时间")
    private LocalDateTime updateTime;
    
    
    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
    
}
