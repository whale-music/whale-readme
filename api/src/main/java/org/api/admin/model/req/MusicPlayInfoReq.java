package org.api.admin.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicPlayInfoReq {
    private List<Long> ids;
    // 是否添加不可播放音乐
    private Boolean isPlayed = true;
}
