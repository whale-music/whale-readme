package org.api.model.song;

public class Pc {
    private Integer br;
    private Integer uid;
    private String ar;
    private String alb;
    private String nickname;
    private String fn;
    private String sn;
    private String cid;
    
    public Integer getBr() {
        return br;
    }
    
    public void setBr(Integer br) {
        this.br = br;
    }
    
    public Integer getUid() {
        return uid;
    }
    
    public void setUid(Integer uid) {
        this.uid = uid;
    }
    
    public String getAr() {
        return ar;
    }
    
    public void setAr(String ar) {
        this.ar = ar;
    }
    
    public String getAlb() {
        return alb;
    }
    
    public void setAlb(String alb) {
        this.alb = alb;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getFn() {
        return fn;
    }
    
    public void setFn(String fn) {
        this.fn = fn;
    }
    
    public String getSn() {
        return sn;
    }
    
    public void setSn(String sn) {
        this.sn = sn;
    }
    
    public String getCid() {
        return cid;
    }
    
    public void setCid(String cid) {
        this.cid = cid;
    }
    
    @Override
    public String toString() {
        return
                "Pc{" +
                        "br = '" + br + '\'' +
                        ",uid = '" + uid + '\'' +
                        ",ar = '" + ar + '\'' +
                        ",alb = '" + alb + '\'' +
                        ",nickname = '" + nickname + '\'' +
                        ",fn = '" + fn + '\'' +
                        ",sn = '" + sn + '\'' +
                        ",cid = '" + cid + '\'' +
                        "}";
    }
}
