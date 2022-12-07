package org.api.model.song;

import java.util.List;

public class OriginSongSimpleData{
	private List<ArtistsItem> artists;
	private String name;
	private Integer songId;
	private AlbumMeta albumMeta;

	public List<ArtistsItem> getArtists(){
		return artists;
	}

	public void setArtists(List<ArtistsItem> artists){
		this.artists = artists;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public Integer getSongId(){
		return songId;
	}

	public void setSongId(Integer songId){
		this.songId = songId;
	}

	public AlbumMeta getAlbumMeta(){
		return albumMeta;
	}

	public void setAlbumMeta(AlbumMeta albumMeta){
		this.albumMeta = albumMeta;
	}

	@Override
 	public String toString(){
		return 
			"OriginSongSimpleData{" + 
			"artists = '" + artists + '\'' + 
			",name = '" + name + '\'' + 
			",songId = '" + songId + '\'' + 
			",albumMeta = '" + albumMeta + '\'' + 
			"}";
		}
}