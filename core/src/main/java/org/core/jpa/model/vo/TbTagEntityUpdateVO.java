package org.core.jpa.model.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel("更新 标签表（风格）")
@EqualsAndHashCode(callSuper = false)
public class TbTagEntityUpdateVO extends TbTagEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
