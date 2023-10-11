package org.api.neteasecloudmusic.model.vo.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Profile {
    private String backgroundUrl;
    private Long birthday;
    private Object detailDescription;
    private Boolean authenticated;
    private Integer gender;
    private String signature;
    private Integer city;
    private Object description;
    private Object remarkName;
    private String shortUserName;
    private Integer accountStatus;
    private Integer locationStatus;
    private Long avatarImgId;
    private Boolean defaultAvatar;
    private Integer province;
    private String nickname;
    private Object expertTags;
    private Integer djStatus;
    private String avatarUrl;
    private Integer accountType;
    private Integer authStatus;
    private Integer vipType;
    private String userName;
    private Long userId;
    private Boolean followed;
    private String lastLoginIP;
    private LocalDateTime lastLoginTime;
    private Integer authenticationTypes;
    private Boolean mutual;
    private Long createTime;
    private Integer authority;
    private Boolean anchor;
    private Integer userType;
    private Long backgroundImgId;
    private Integer viptypeVersion;
    private Object experts;
    private Object avatarDetail;
}