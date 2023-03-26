package org.api.neteasecloudmusic.model.vo.artist.album;

import lombok.Data;

import java.util.List;

@Data
public class ArtistsItem{
	private String img1v1Url;
	private int musicSize;
	private String img1v1IdStr;
	private long img1v1Id;
	private boolean followed;
	private int albumSize;
	private String picUrl;
	private int topicPerson;
	private String briefDesc;
	private String name;
	private List<Object> alias;
	private Long id;
	private int picId;
	private String trans;
}