package org.api.model.singer;

import java.util.List;

public class Data{
	private int videoCount;
	private Artist artist;
	private boolean blacklist;
	private int preferShow;
	private boolean showPriMsg;
	private List<SecondaryExpertIdentiyItem> secondaryExpertIdentiy;

	public int getVideoCount(){
		return videoCount;
	}

	public void setVideoCount(int videoCount){
		this.videoCount = videoCount;
	}

	public Artist getArtist(){
		return artist;
	}

	public void setArtist(Artist artist){
		this.artist = artist;
	}

	public boolean isBlacklist(){
		return blacklist;
	}

	public void setBlacklist(boolean blacklist){
		this.blacklist = blacklist;
	}

	public int getPreferShow(){
		return preferShow;
	}

	public void setPreferShow(int preferShow){
		this.preferShow = preferShow;
	}

	public boolean isShowPriMsg(){
		return showPriMsg;
	}

	public void setShowPriMsg(boolean showPriMsg){
		this.showPriMsg = showPriMsg;
	}

	public List<SecondaryExpertIdentiyItem> getSecondaryExpertIdentiy(){
		return secondaryExpertIdentiy;
	}

	public void setSecondaryExpertIdentiy(List<SecondaryExpertIdentiyItem> secondaryExpertIdentiy){
		this.secondaryExpertIdentiy = secondaryExpertIdentiy;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"videoCount = '" + videoCount + '\'' + 
			",artist = '" + artist + '\'' + 
			",blacklist = '" + blacklist + '\'' + 
			",preferShow = '" + preferShow + '\'' + 
			",showPriMsg = '" + showPriMsg + '\'' + 
			",secondaryExpertIdentiy = '" + secondaryExpertIdentiy + '\'' + 
			"}";
		}
}