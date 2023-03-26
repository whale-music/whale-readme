package org.api.neteasecloudmusic.model.vo.album.detail;

import lombok.Data;

@Data
public class Album{
	private String albumName;
	private String artistAvatarUrl;
	private int roomNo;
	private Object artist;
	private int liveType;
	private Long albumId;
	private Long artistId;
	private int bundling;
	private boolean stars;
	private String coverUrl;
	private String artistNames;
	private Object customPriceConfig;
	private Object artistInfoList;
	private String artistName;
	private String blurImgUrl;
}