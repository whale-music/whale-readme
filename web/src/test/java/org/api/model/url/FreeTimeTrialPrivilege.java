package org.api.model.url;

public class FreeTimeTrialPrivilege{
	private boolean userConsumable;
	private boolean resConsumable;
	private int remainTime;
	private int type;

	public void setUserConsumable(boolean userConsumable){
		this.userConsumable = userConsumable;
	}

	public boolean isUserConsumable(){
		return userConsumable;
	}

	public void setResConsumable(boolean resConsumable){
		this.resConsumable = resConsumable;
	}

	public boolean isResConsumable(){
		return resConsumable;
	}

	public void setRemainTime(int remainTime){
		this.remainTime = remainTime;
	}

	public int getRemainTime(){
		return remainTime;
	}

	public void setType(int type){
		this.type = type;
	}

	public int getType(){
		return type;
	}
}
