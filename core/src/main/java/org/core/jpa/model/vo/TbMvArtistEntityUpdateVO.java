package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@Schema(title = "更新 mv和歌手中间表")
@EqualsAndHashCode(callSuper = false)
public class TbMvArtistEntityUpdateVO extends TbMvArtistEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
