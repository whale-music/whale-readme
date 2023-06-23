package org.core.jpa.model.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel("更新 歌手表")
@EqualsAndHashCode(callSuper = false)
public class TbArtistEntityUpdateVO extends TbArtistEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
