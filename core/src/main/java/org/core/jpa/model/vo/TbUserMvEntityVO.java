package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;


@Data
@Schema(title = "保存 用户收藏mv")
public class TbUserMvEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 用户ID
     */
    @NotNull(message = "userId can not null")
    @Schema(name = "用户ID")
    private Long userId;
    
    
    /**
     * MV ID
     */
    @NotNull(message = "mvId can not null")
    @Schema(name = "MV ID")
    private Long mvId;
    
}
