package org.api.neteasecloudmusic.model.vo.user;

import lombok.Data;

@Data
public class UserVo {
    private int code;
    private Profile profile;
    private Account account;
}