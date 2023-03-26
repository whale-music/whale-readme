package org.api.model.song;

public class FreeTrialPrivilege {
    private Boolean userConsumable;
    private Boolean resConsumable;
    private Object listenType;
    
    public void setUserConsumable(Boolean userConsumable) {
        this.userConsumable = userConsumable;
    }
    
    public Boolean isUserConsumable() {
        return userConsumable;
    }
    
    public void setResConsumable(Boolean resConsumable) {
        this.resConsumable = resConsumable;
    }
    
    public Boolean isResConsumable() {
        return resConsumable;
    }
    
    public Object getListenType() {
        return listenType;
    }
    
    public void setListenType(Object listenType) {
        this.listenType = listenType;
    }
    
    @Override
    public String toString() {
        return
                "FreeTrialPrivilege{" +
                        "userConsumable = '" + userConsumable + '\'' +
                        ",resConsumable = '" + resConsumable + '\'' +
                        ",listenType = '" + listenType + '\'' +
                        "}";
    }
}
