package org.plugin.service.sync.impl.music163.model.song;

public class NoCopyrightRcmd{
	private String typeDesc;
	private Integer type;
	private Object songId;

	public String getTypeDesc(){
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc){
		this.typeDesc = typeDesc;
	}

	public Integer getType(){
		return type;
	}

	public void setType(Integer type){
		this.type = type;
	}

	public Object getSongId(){
		return songId;
	}

	public void setSongId(Object songId){
		this.songId = songId;
	}

	@Override
 	public String toString(){
		return 
			"NoCopyrightRcmd{" + 
			"typeDesc = '" + typeDesc + '\'' + 
			",type = '" + type + '\'' + 
			",songId = '" + songId + '\'' + 
			"}";
		}
}
