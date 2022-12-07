package org.api.model.song;

import java.util.List;

public class ArItem{
	private String name;
	private List<Object> tns;
	private List<Object> alias;
	private Integer id;

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

	public List<Object> getAlias(){
		return alias;
	}

	public void setAlias(List<Object> alias){
		this.alias = alias;
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
			"ArItem{" + 
			"name = '" + name + '\'' + 
			",tns = '" + tns + '\'' + 
			",alias = '" + alias + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}