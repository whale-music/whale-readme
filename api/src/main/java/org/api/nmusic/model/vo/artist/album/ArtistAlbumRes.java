package org.api.nmusic.model.vo.artist.album;

import lombok.Data;

import java.util.List;

@Data
public class ArtistAlbumRes{
	private int code;
	private Artist artist;
	private boolean more;
	private List<HotAlbumsItem> hotAlbums;
}