package org.api.nmusic.model.vo.album.detail;

import lombok.Data;

@Data
public class AlbumDetailRes {
	private Product product;
	private int code;
	private Album album;
	private int vipType;
	private Style style;
	private Board board;
	private int boughtCnt;
	private boolean canBuy;
	private boolean hasAlbum;
	private long visitorId;
	private int singleSongProductId;
}