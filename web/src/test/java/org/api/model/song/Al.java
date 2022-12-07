package org.api.model.song;

import java.util.List;

public class Al{
	private String picUrl;
	private String name;
	private List<Object> tns;
	private String picStr;
	private Integer id;
	private Long pic;

	public String getPicUrl(){
		return picUrl;
	}

	public void setPicUrl(String picUrl){
		this.picUrl = picUrl;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public List<Object> getTns(){
		return tns;
	}

	public void setTns(List<Object> tns){
		this.tns = tns;
	}

	public String getPicStr(){
		return picStr;
	}

	public void setPicStr(String picStr){
		this.picStr = picStr;
	}

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Long getPic(){
		return pic;
	}

	public void setPic(Long pic){
		this.pic = pic;
	}

	@Override
 	public String toString(){
		return 
			"Al{" + 
			"picUrl = '" + picUrl + '\'' + 
			",name = '" + name + '\'' + 
			",tns = '" + tns + '\'' + 
			",pic_str = '" + picStr + '\'' + 
			",id = '" + id + '\'' + 
			",pic = '" + pic + '\'' + 
			"}";
		}
}