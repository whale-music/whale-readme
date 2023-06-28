package org.core.jpa.model.dto;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("mv和歌手中间表")
public class TbMvArtistEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long mvId;
    
    private Long artistId;
    
}
