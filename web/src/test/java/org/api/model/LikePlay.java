package org.api.model;

import java.util.List;

public class LikePlay {
    private int code;
    private List<Long> ids;
    private long checkPoint;
    
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public List<Long> getIds() {
        return ids;
    }
    
    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
    
    public long getCheckPoint() {
        return checkPoint;
    }
    
    public void setCheckPoint(long checkPoint) {
        this.checkPoint = checkPoint;
    }
}