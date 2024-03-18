package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "自定义查询 mv和歌手中间表")
public class TbMvArtistEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long mvId;
    
    private Long artistId;
    
}
