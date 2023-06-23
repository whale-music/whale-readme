package org.core.jpa.model.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel("更新 音乐专辑歌单封面表")
@EqualsAndHashCode(callSuper = false)
public class TbPicEntityUpdateVO extends TbPicEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
