package org.api.model.album;

public class Al {
    private String picUrl;
    private String name;
    private String picStr;
    private int id;
    private long pic;
    
    public String getPicUrl() {
        return picUrl;
    }
    
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPicStr() {
        return picStr;
    }
    
    public void setPicStr(String picStr) {
        this.picStr = picStr;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public long getPic() {
        return pic;
    }
    
    public void setPic(long pic) {
        this.pic = pic;
    }
    
    @Override
    public String toString() {
        return
                "Al{" +
                        "picUrl = '" + picUrl + '\'' +
                        ",name = '" + name + '\'' +
                        ",pic_str = '" + picStr + '\'' +
                        ",id = '" + id + '\'' +
                        ",pic = '" + pic + '\'' +
                        "}";
    }
}
