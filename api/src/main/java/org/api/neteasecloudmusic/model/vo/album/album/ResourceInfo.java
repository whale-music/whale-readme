package org.api.neteasecloudmusic.model.vo.album.album;

import lombok.Data;

@Data
public class ResourceInfo{
	private String imgUrl;
	private Object creator;
	private Object subTitle;
	private Object webUrl;
	private String name;
	private int id;
	private int userId;
	private Object encodedId;
}