package org.core.jpa.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(title = "音乐专辑歌单封面表")
public class TbPicEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    
    
    /**
     * 音乐网络地址，或路径
     */
    @Schema(name = "音乐网络地址，或路径")
    private String url;
    
    private String md5;
    
    
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
