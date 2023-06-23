package org.core.jpa.model.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel("更新 音乐播放历史(包括歌单，音乐，专辑）")
@EqualsAndHashCode(callSuper = false)
public class TbHistoryEntityUpdateVO extends TbHistoryEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
