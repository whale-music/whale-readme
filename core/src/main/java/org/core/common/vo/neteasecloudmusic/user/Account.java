package org.core.common.vo.neteasecloudmusic.user;

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
    private Integer tokenVersion;
    private Long id;
    private int whitelistAuthority;
    private int baoyueVersion;
    private int donateVersion;
    private int status;
}