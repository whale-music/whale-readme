package org.api.neteasecloudmusic.model.vo.album.album;

import lombok.Data;

import java.util.List;

@Data
public class Album{
	private Artist artist;
	private String description;
	private long pic;
	private String type;
	private Object awardTags;
	private String picUrl;
	private List<ArtistsItem> artists;
	private String briefDesc;
	private boolean onSale;
	private List<String> alias;
	private String company;
	private Long id;
	private long picId;
	private Info info;
	private long publishTime;
	private String picIdStr;
	private String blurPicUrl;
	private String commentThreadId;
	private String tags;
	private int companyId;
	private int size;
	private int copyrightId;
	private List<Object> songs;
	private boolean paid;
	private String name;
	private String subType;
	private int mark;
	private int status;
}