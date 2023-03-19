package org.api.neteasecloudmusic.model.vo.login.status;

import lombok.Data;

@Data
public class DataJson {
    private int code;
    private Profile profile;
    private Account account;
}