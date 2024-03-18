package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;


@Data
@Schema(title = "保存 音乐来源")
public class TbOriginEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "id can not null")
    private Long id;
    
    
    /**
     * 音乐ID
     */
    @NotNull(message = "musicId can not null")
    @Schema(name = "音乐ID")
    private Long musicId;
    
    
    /**
     * 源地址ID
     */
    @NotNull(message = "resourceId can not null")
    @Schema(name = "源地址ID")
    private Long resourceId;
    
    
    /**
     * 来源
     */
    @NotNull(message = "origin can not null")
    @Schema(name = "来源")
    private String origin;
    
    
    /**
     * 来源地址
     */
    @Schema(name = "来源地址")
    private String originUrl;
    
}
