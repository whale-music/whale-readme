package org.api.model.album;

public class ChargeInfoListItem {
    private int rate;
    private Object chargeMessage;
    private int chargeType;
    private Object chargeUrl;
    
    public int getRate() {
        return rate;
    }
    
    public void setRate(int rate) {
        this.rate = rate;
    }
    
    public Object getChargeMessage() {
        return chargeMessage;
    }
    
    public void setChargeMessage(Object chargeMessage) {
        this.chargeMessage = chargeMessage;
    }
    
    public int getChargeType() {
        return chargeType;
    }
    
    public void setChargeType(int chargeType) {
        this.chargeType = chargeType;
    }
    
    public Object getChargeUrl() {
        return chargeUrl;
    }
    
    public void setChargeUrl(Object chargeUrl) {
        this.chargeUrl = chargeUrl;
    }
    
    @Override
    public String toString() {
        return
                "ChargeInfoListItem{" +
                        "rate = '" + rate + '\'' +
                        ",chargeMessage = '" + chargeMessage + '\'' +
                        ",chargeType = '" + chargeType + '\'' +
                        ",chargeUrl = '" + chargeUrl + '\'' +
                        "}";
    }
}
