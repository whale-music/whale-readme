package org.api.neteasecloudmusic.model.vo.user.detail;

import lombok.Data;

import java.util.List;

@Data
public class Profile {
    private String backgroundUrl;
    private long birthday;
    private String detailDescription;
    private PrivacyItemUnlimit privacyItemUnlimit;
    private int gender;
    private int city;
    private String signature;
    private int followeds;
    private String description;
    private Object remarkName;
    private int eventCount;
    private int allSubscribedCount;
    private int playlistBeSubscribedCount;
    private int accountStatus;
    private long avatarImgId;
    private boolean defaultAvatar;
    private String backgroundImgIdStr;
    private String avatarImgIdStr;
    private int province;
    private boolean followMe;
    private List<Object> artistIdentity;
    private String nickname;
    private Object expertTags;
    private int sDJPCount;
    private int newFollows;
    private int djStatus;
    private String avatarUrl;
    private int authStatus;
    private int follows;
    private int vipType;
    private boolean blacklist;
    private Long userId;
    private boolean followed;
    private boolean mutual;
    private Object followTime;
    private long createTime;
    private int authority;
    private int cCount;
    private boolean inBlacklist;
    private long backgroundImgId;
    private int userType;
    private Object avatarDetail;
    private Experts experts;
    private int playlistCount;
    private int sCount;
}