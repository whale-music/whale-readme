package org.api.neteasecloudmusic.model.vo.user.detail;

import lombok.Data;

import java.util.List;

@Data
public class UserDetailRes {
    private UserPoint userPoint;
    private int code;
    private int level;
    private int listenSongs;
    private int createDays;
    private Profile profile;
    private List<BindingsItem> bindings;
    private boolean pcSign;
    private boolean mobileSign;
    private ProfileVillageInfo profileVillageInfo;
    private boolean adValid;
    private long createTime;
    private boolean recallUser;
    private boolean newUser;
    private boolean peopleCanSeeMyPlayRecord;
}