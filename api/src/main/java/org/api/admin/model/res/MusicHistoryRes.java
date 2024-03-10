package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.common.constant.HistoryConstant;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicHistoryRes {
    private Long id;
    private String name;
    private Long middleId;
    private String type;
    private Integer count;
    private Long duration;
    private Long userId;
    private String nickname;
    private LocalDateTime updateTime;
    
    public MusicHistoryRes(Long id, String name, Long middleId, Byte type, Integer count, Long duration, Long userId, String nickname, LocalDateTime updateTime) {
        this.id = id;
        this.name = name;
        this.middleId = middleId;
        this.type = HistoryConstant.map.get(type);
        this.count = count;
        this.duration = duration;
        this.userId = userId;
        this.nickname = nickname;
        this.updateTime = updateTime;
    }
    
    public void setType(Byte type) {
        this.type = HistoryConstant.map.get(type);
    }
}
