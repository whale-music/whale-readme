package org.plugin.service.sync.impl.music163.model.song;

public class ArtistsItem{
	private String name;
	private Integer id;

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	@Override
 	public String toString(){
		return 
			"ArtistsItem{" + 
			"name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}
