package org.api.nmusic.model.vo.album.album;

import lombok.Data;

import java.util.List;

@Data
public class AlbumRes{
	private int code;
	private List<SongsItem> songs;
	private Album album;
	private boolean resourceState;
}