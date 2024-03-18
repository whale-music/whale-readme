package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@Schema(title = "更新 音乐来源")
@EqualsAndHashCode(callSuper = false)
public class TbOriginEntityUpdateVO extends TbOriginEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
