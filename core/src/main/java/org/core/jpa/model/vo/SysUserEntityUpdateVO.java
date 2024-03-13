package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@Schema(title = "更新 系统用户表")
@EqualsAndHashCode(callSuper = false)
public class SysUserEntityUpdateVO extends SysUserEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
