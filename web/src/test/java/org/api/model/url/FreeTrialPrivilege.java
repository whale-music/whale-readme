package org.api.model.url;

public class FreeTrialPrivilege {
    private boolean userConsumable;
    private boolean resConsumable;
    private Object cannotListenReason;
    private Object listenType;
    
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
    
    public Object getCannotListenReason() {
        return cannotListenReason;
    }
    
    public void setCannotListenReason(Object cannotListenReason) {
        this.cannotListenReason = cannotListenReason;
    }
    
    public Object getListenType() {
        return listenType;
    }
    
    public void setListenType(Object listenType) {
        this.listenType = listenType;
    }
}
