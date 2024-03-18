package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@Schema(title = "更新 音乐播放历史(包括歌单，音乐，专辑）")
@EqualsAndHashCode(callSuper = false)
public class TbHistoryEntityUpdateVO extends TbHistoryEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
