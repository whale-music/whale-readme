package org.core.jpa.model.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel("更新 存储地址")
@EqualsAndHashCode(callSuper = false)
public class TbResourceEntityUpdateVO extends TbResourceEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
