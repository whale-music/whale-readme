package org.api.model.song;

public class ChargeInfoListItem{
	private Integer rate;
	private Object chargeMessage;
	private Integer chargeType;
	private Object chargeUrl;

	public Integer getRate(){
		return rate;
	}

	public void setRate(Integer rate){
		this.rate = rate;
	}

	public Object getChargeMessage(){
		return chargeMessage;
	}

	public void setChargeMessage(Object chargeMessage){
		this.chargeMessage = chargeMessage;
	}

	public Integer getChargeType(){
		return chargeType;
	}

	public void setChargeType(Integer chargeType){
		this.chargeType = chargeType;
	}

	public Object getChargeUrl(){
		return chargeUrl;
	}

	public void setChargeUrl(Object chargeUrl){
		this.chargeUrl = chargeUrl;
	}

	@Override
 	public String toString(){
		return 
			"ChargeInfoListItem{" + 
			"rate = '" + rate + '\'' + 
			",chargeMessage = '" + chargeMessage + '\'' + 
			",chargeType = '" + chargeType + '\'' + 
			",chargeUrl = '" + chargeUrl + '\'' + 
			"}";
		}
}
