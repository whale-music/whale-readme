package org.api.nmusic.model.vo.toplist.playlist;

import lombok.Data;

import java.util.List;

@Data
public class TopListPlayListRes{
	private int total;
	private int code;
	private boolean more;
	private String cat;
	private List<PlaylistsItem> playlists;
}