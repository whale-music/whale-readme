package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@Schema(title = "更新 用户收藏歌单")
@EqualsAndHashCode(callSuper = false)
public class TbUserCollectEntityUpdateVO extends TbUserCollectEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
