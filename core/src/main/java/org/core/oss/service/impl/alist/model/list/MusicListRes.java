package org.core.oss.service.impl.alist.model.list;

public class MusicListRes {
	private Integer code;
	private MusicDataItem musicDataItem;
	private String message;
	
	public Integer getCode() {
		return code;
	}
	
	public void setCode(Integer code) {
		this.code = code;
	}
	
	public MusicDataItem getData() {
		return musicDataItem;
	}
	
	public void setData(MusicDataItem musicDataItem) {
		this.musicDataItem = musicDataItem;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
