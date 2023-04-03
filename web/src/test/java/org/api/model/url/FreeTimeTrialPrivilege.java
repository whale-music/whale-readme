package org.api.model.url;

public class FreeTimeTrialPrivilege {
    private boolean userConsumable;
    private boolean resConsumable;
    private int remainTime;
    private int type;
    
    public boolean isUserConsumable() {
        return userConsumable;
    }
    
    public void setUserConsumable(boolean userConsumable) {
        this.userConsumable = userConsumable;
    }
    
    public boolean isResConsumable() {
        return resConsumable;
    }
    
    public void setResConsumable(boolean resConsumable) {
        this.resConsumable = resConsumable;
    }
    
    public int getRemainTime() {
        return remainTime;
    }
    
    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
}
