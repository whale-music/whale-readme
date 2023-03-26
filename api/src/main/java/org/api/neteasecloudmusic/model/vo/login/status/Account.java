package org.api.neteasecloudmusic.model.vo.login.status;

import lombok.Data;

@Data
public class Account {
    private int vipType;
    private String userName;
    private int type;
    private boolean paidFee;
    private int ban;
    private boolean anonimousUser;
    private long createTime;
    private int tokenVersion;
    private long id;
    private int whitelistAuthority;
    private int baoyueVersion;
    private int donateVersion;
    private int status;
}