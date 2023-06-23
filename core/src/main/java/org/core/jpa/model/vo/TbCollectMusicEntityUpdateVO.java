package org.core.jpa.model.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel("更新 歌单和音乐的中间表，用于记录歌单中的每一个音乐")
@EqualsAndHashCode(callSuper = false)
public class TbCollectMusicEntityUpdateVO extends TbCollectMusicEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
