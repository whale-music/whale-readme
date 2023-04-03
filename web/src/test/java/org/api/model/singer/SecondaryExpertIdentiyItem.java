package org.api.model.singer;

public class SecondaryExpertIdentiyItem {
    private String expertIdentiyName;
    private int expertIdentiyId;
    private int expertIdentiyCount;
    
    public String getExpertIdentiyName() {
        return expertIdentiyName;
    }
    
    public void setExpertIdentiyName(String expertIdentiyName) {
        this.expertIdentiyName = expertIdentiyName;
    }
    
    public int getExpertIdentiyId() {
        return expertIdentiyId;
    }
    
    public void setExpertIdentiyId(int expertIdentiyId) {
        this.expertIdentiyId = expertIdentiyId;
    }
    
    public int getExpertIdentiyCount() {
        return expertIdentiyCount;
    }
    
    public void setExpertIdentiyCount(int expertIdentiyCount) {
        this.expertIdentiyCount = expertIdentiyCount;
    }
    
    @Override
    public String toString() {
        return
                "SecondaryExpertIdentiyItem{" +
                        "expertIdentiyName = '" + expertIdentiyName + '\'' +
                        ",expertIdentiyId = '" + expertIdentiyId + '\'' +
                        ",expertIdentiyCount = '" + expertIdentiyCount + '\'' +
                        "}";
    }
}
