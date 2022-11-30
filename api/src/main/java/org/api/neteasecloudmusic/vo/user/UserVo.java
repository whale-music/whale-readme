package org.api.neteasecloudmusic.vo.user;

import lombok.Data;

@Data
public class UserVo {
    private int code;
    private Profile profile;
    private Account account;
}