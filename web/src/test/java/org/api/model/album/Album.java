package org.api.model.album;

import java.util.List;

public class Album{
	private Artist artist;
	private String description;
	private long pic;
	private String type;
	private Object awardTags;
	private String picUrl;
	private List<ArtistsItem> artists;
	private String briefDesc;
	private boolean onSale;
	private List<Object> alias;
	private String company;
	private int id;
	private long picId;
	private Info info;
	private long publishTime;
	private String picIdStr;
	private String blurPicUrl;
	private String commentThreadId;
	private String tags;
	private int companyId;
	private int size;
	private int copyrightId;
	private List<Object> songs;
	private boolean paid;
	private String name;
	private String subType;
	private int mark;
	private int status;

	public Artist getArtist(){
		return artist;
	}

	public void setArtist(Artist artist){
		this.artist = artist;
	}

	public String getDescription(){
		return description;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public long getPic(){
		return pic;
	}

	public void setPic(long pic){
		this.pic = pic;
	}

	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
	}

	public Object getAwardTags(){
		return awardTags;
	}

	public void setAwardTags(Object awardTags){
		this.awardTags = awardTags;
	}

	public String getPicUrl(){
		return picUrl;
	}

	public void setPicUrl(String picUrl){
		this.picUrl = picUrl;
	}

	public List<ArtistsItem> getArtists(){
		return artists;
	}

	public void setArtists(List<ArtistsItem> artists){
		this.artists = artists;
	}

	public String getBriefDesc(){
		return briefDesc;
	}

	public void setBriefDesc(String briefDesc){
		this.briefDesc = briefDesc;
	}

	public boolean isOnSale(){
		return onSale;
	}

	public void setOnSale(boolean onSale){
		this.onSale = onSale;
	}

	public List<Object> getAlias(){
		return alias;
	}

	public void setAlias(List<Object> alias){
		this.alias = alias;
	}

	public String getCompany(){
		return company;
	}

	public void setCompany(String company){
		this.company = company;
	}

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public long getPicId(){
		return picId;
	}

	public void setPicId(long picId){
		this.picId = picId;
	}

	public Info getInfo(){
		return info;
	}

	public void setInfo(Info info){
		this.info = info;
	}

	public long getPublishTime(){
		return publishTime;
	}

	public void setPublishTime(long publishTime){
		this.publishTime = publishTime;
	}

	public String getPicIdStr(){
		return picIdStr;
	}

	public void setPicIdStr(String picIdStr){
		this.picIdStr = picIdStr;
	}

	public String getBlurPicUrl(){
		return blurPicUrl;
	}

	public void setBlurPicUrl(String blurPicUrl){
		this.blurPicUrl = blurPicUrl;
	}

	public String getCommentThreadId(){
		return commentThreadId;
	}

	public void setCommentThreadId(String commentThreadId){
		this.commentThreadId = commentThreadId;
	}

	public String getTags(){
		return tags;
	}

	public void setTags(String tags){
		this.tags = tags;
	}

	public int getCompanyId(){
		return companyId;
	}

	public void setCompanyId(int companyId){
		this.companyId = companyId;
	}

	public int getSize(){
		return size;
	}

	public void setSize(int size){
		this.size = size;
	}

	public int getCopyrightId(){
		return copyrightId;
	}

	public void setCopyrightId(int copyrightId){
		this.copyrightId = copyrightId;
	}

	public List<Object> getSongs(){
		return songs;
	}

	public void setSongs(List<Object> songs){
		this.songs = songs;
	}

	public boolean isPaid(){
		return paid;
	}

	public void setPaid(boolean paid){
		this.paid = paid;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getSubType(){
		return subType;
	}

	public void setSubType(String subType){
		this.subType = subType;
	}

	public int getMark(){
		return mark;
	}

	public void setMark(int mark){
		this.mark = mark;
	}

	public int getStatus(){
		return status;
	}

	public void setStatus(int status){
		this.status = status;
	}

	@Override
 	public String toString(){
		return 
			"Album{" + 
			"artist = '" + artist + '\'' + 
			",description = '" + description + '\'' + 
			",pic = '" + pic + '\'' + 
			",type = '" + type + '\'' + 
			",awardTags = '" + awardTags + '\'' + 
			",picUrl = '" + picUrl + '\'' + 
			",artists = '" + artists + '\'' + 
			",briefDesc = '" + briefDesc + '\'' + 
			",onSale = '" + onSale + '\'' + 
			",alias = '" + alias + '\'' + 
			",company = '" + company + '\'' + 
			",id = '" + id + '\'' + 
			",picId = '" + picId + '\'' + 
			",info = '" + info + '\'' + 
			",publishTime = '" + publishTime + '\'' + 
			",picId_str = '" + picIdStr + '\'' + 
			",blurPicUrl = '" + blurPicUrl + '\'' + 
			",commentThreadId = '" + commentThreadId + '\'' + 
			",tags = '" + tags + '\'' + 
			",companyId = '" + companyId + '\'' + 
			",size = '" + size + '\'' + 
			",copyrightId = '" + copyrightId + '\'' + 
			",songs = '" + songs + '\'' + 
			",paid = '" + paid + '\'' + 
			",name = '" + name + '\'' + 
			",subType = '" + subType + '\'' + 
			",mark = '" + mark + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}