package org.core.model.res;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel("更新 歌曲专辑表")
@EqualsAndHashCode(callSuper = false)
public class TbAlbumPojoUpdateRes extends TbAlbumPojoRes implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
