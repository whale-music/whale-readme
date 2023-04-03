package org.core.model.res;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel("更新 用户收藏专辑表")
@EqualsAndHashCode(callSuper = false)
public class TbUserAlbumPojoUpdateRes extends TbUserAlbumPojoRes implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
