package org.api.model.url;

import java.util.List;

public class SongUrl{
	private int code;
	private List<DataItem> data;

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}
}