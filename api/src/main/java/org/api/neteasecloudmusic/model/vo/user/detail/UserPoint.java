package org.api.neteasecloudmusic.model.vo.user.detail;

import lombok.Data;

@Data
public class UserPoint {
    private int balance;
    private int blockBalance;
    private long updateTime;
    private Long userId;
    private int version;
    private int status;
}