package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "自定义查询 音乐来源")
public class TbOriginEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    
    /**
     * 音乐ID
     */
    @Schema(name = "音乐ID")
    private Long musicId;
    
    
    /**
     * 源地址ID
     */
    @Schema(name = "源地址ID")
    private Long resourceId;
    
    
    /**
     * 来源
     */
    @Schema(name = "来源")
    private String origin;
    
    
    /**
     * 来源地址
     */
    @Schema(name = "来源地址")
    private String originUrl;
    
}
