package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Schema(title = "保存 歌单列表")
public class TbCollectEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 歌单表ID
     */
    @NotNull(message = "id can not null")
    @Schema(name = "歌单表ID")
    private Long id;
    
    
    /**
     * 歌单名（包括用户喜爱歌单）
     */
    @NotNull(message = "playListName can not null")
    @Schema(name = "歌单名（包括用户喜爱歌单）")
    private String playListName;
    
    
    /**
     * 歌单类型，0为普通歌单，1为用户喜爱歌单，2为推荐歌单
     */
    @NotNull(message = "type can not null")
    @Schema(name = "歌单类型，0为普通歌单，1为用户喜爱歌单，2为推荐歌单")
    private Integer type;
    
    
    /**
     * 简介
     */
    @Schema(name = "简介")
    private String description;
    
    
    /**
     * 创建人ID
     */
    @Schema(name = "创建人ID")
    private Long userId;
    
    
    /**
     * 排序字段
     */
    @Schema(name = "排序字段")
    private Long sort;
    
    
    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 修改时间
     */
    @Schema(name = "修改时间")
    private LocalDateTime updateTime;
    
}
