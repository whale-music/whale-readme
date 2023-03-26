package org.api.model.album;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Album {
	private Artist artist;
	private String description;
	private Long pic;
	private String type;
	private Object awardTags;
	private String picUrl;
	private List<ArtistsItem> artists;
	private String briefDesc;
	private Boolean onSale;
	private List<Object> alias;
	private String company;
	private Integer id;
	private Long picId;
	private Info info;
	private Long publishTime;
	private String picIdStr;
	private String blurPicUrl;
	private String commentThreadId;
	private String tags;
	private Integer companyId;
	private Integer size;
	private Integer copyrightId;
	private List<Object> songs;
	private Boolean paid;
	private String name;
	private String subType;
	private Integer mark;
	private Integer status;
}