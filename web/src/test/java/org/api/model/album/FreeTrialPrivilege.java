package org.api.model.album;

public class FreeTrialPrivilege{
	private boolean userConsumable;
	private boolean resConsumable;
	private Object listenType;

	public boolean isUserConsumable(){
		return userConsumable;
	}

	public void setUserConsumable(boolean userConsumable){
		this.userConsumable = userConsumable;
	}

	public boolean isResConsumable(){
		return resConsumable;
	}

	public void setResConsumable(boolean resConsumable){
		this.resConsumable = resConsumable;
	}

	public Object getListenType(){
		return listenType;
	}

	public void setListenType(Object listenType){
		this.listenType = listenType;
	}

	@Override
 	public String toString(){
		return 
			"FreeTrialPrivilege{" + 
			"userConsumable = '" + userConsumable + '\'' + 
			",resConsumable = '" + resConsumable + '\'' + 
			",listenType = '" + listenType + '\'' + 
			"}";
		}
}
