package org.core.jpa.model.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel("更新 用户收藏歌单")
@EqualsAndHashCode(callSuper = false)
public class TbUserCollectEntityUpdateVO extends TbUserCollectEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
