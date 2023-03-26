package org.api.model.singer;

public class SingerRes{
	private int code;
	private Data data;
	private String message;

	public int getCode(){
		return code;
	}

	public void setCode(int code){
		this.code = code;
	}

	public Data getData(){
		return data;
	}

	public void setData(Data data){
		this.data = data;
	}

	public String getMessage(){
		return message;
	}

	public void setMessage(String message){
		this.message = message;
	}

	@Override
 	public String toString(){
		return 
			"ArtistRes{" +
					"code = '" + code + '\'' +
					",data = '" + data + '\'' +
					",message = '" + message + '\'' +
					"}";
		}
}
