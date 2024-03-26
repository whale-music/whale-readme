package org.api.nmusic.model.vo.toplist.artist;

import lombok.Data;

import java.util.List;

@Data
public class ArtistsItem{
	private Integer lastRank;
	private String img1v1Url;
	private String picIdStr;
	private Integer musicSize;
	private String img1v1IdStr;
	private Long img1v1Id;
	private Integer albumSize;
	private String picUrl;
	private Integer score;
	private Integer topicPerson;
	private String briefDesc;
	private String name;
	private List<String> alias;
	private Long id;
	private Long picId;
	private String trans;
	private List<String> transNames;
}