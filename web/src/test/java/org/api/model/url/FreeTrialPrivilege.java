package org.api.model.url;

public class FreeTrialPrivilege{
	private boolean userConsumable;
	private boolean resConsumable;
	private Object cannotListenReason;
	private Object listenType;

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

	public void setCannotListenReason(Object cannotListenReason){
		this.cannotListenReason = cannotListenReason;
	}

	public Object getCannotListenReason(){
		return cannotListenReason;
	}

	public void setListenType(Object listenType){
		this.listenType = listenType;
	}

	public Object getListenType(){
		return listenType;
	}
}
