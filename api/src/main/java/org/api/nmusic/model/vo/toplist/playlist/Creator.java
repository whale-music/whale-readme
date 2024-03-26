package org.api.nmusic.model.vo.toplist.playlist;

import lombok.Data;

import java.util.List;

@Data
public class Creator{
	private long birthday;
	private String detailDescription;
	private String backgroundUrl;
	private int gender;
	private int city;
	private String signature;
	private String description;
	private Object remarkName;
	private int accountStatus;
	private long avatarImgId;
	private boolean defaultAvatar;
	private String avatarImgIdStr;
	private String backgroundImgIdStr;
	private int province;
	private String nickname;
	private List<String> expertTags;
	private int djStatus;
	private String avatarUrl;
	private int authStatus;
	private int vipType;
	private boolean followed;
	private int userId;
	private int authenticationTypes;
	private boolean mutual;
	private int authority;
	private boolean anchor;
	private int userType;
	private long backgroundImgId;
	private Object experts;
	private AvatarDetail avatarDetail;
}