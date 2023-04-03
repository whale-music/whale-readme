package org.core.model.res;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel("更新 音乐播放排行榜")
@EqualsAndHashCode(callSuper = false)
public class TbRankPojoUpdateRes extends TbRankPojoRes implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
