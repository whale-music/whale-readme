package org.api.nmusic.model.vo.login.status;

import lombok.Data;

@Data
public class Profile {
    private String backgroundUrl;
    private long birthday;
    private Object detailDescription;
    private boolean authenticated;
    private int gender;
    private Object signature;
    private int city;
    private Object description;
    private Object remarkName;
    private String shortUserName;
    private int accountStatus;
    private int locationStatus;
    private long avatarImgId;
    private boolean defaultAvatar;
    private int province;
    private String nickname;
    private Object expertTags;
    private int djStatus;
    private String avatarUrl;
    private int accountType;
    private int authStatus;
    private int vipType;
    private String userName;
    private long userId;
    private boolean followed;
    private String lastLoginIP;
    private long lastLoginTime;
    private int authenticationTypes;
    private boolean mutual;
    private long createTime;
    private int authority;
    private boolean anchor;
    private int userType;
    private long backgroundImgId;
    private long viptypeVersion;
    private Object experts;
    private Object avatarDetail;
}