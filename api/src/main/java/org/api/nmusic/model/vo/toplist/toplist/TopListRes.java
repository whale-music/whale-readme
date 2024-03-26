package org.api.nmusic.model.vo.toplist.toplist;

import lombok.Data;

import java.util.List;

@Data
public class TopListRes{
	private Integer code;
	private ArtistToplist artistToplist;
	private List<ListItem> list;
}