package org.api.model.album;

public class M {
    private int br;
    private long fid;
    private int size;
    private int vd;
    private int sr;
    
    public int getBr() {
        return br;
    }
    
    public void setBr(int br) {
        this.br = br;
    }
    
    public long getFid() {
        return fid;
    }
    
    public void setFid(long fid) {
        this.fid = fid;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public int getVd() {
        return vd;
    }
    
    public void setVd(int vd) {
        this.vd = vd;
    }
    
    public int getSr() {
        return sr;
    }
    
    public void setSr(int sr) {
        this.sr = sr;
    }
    
    @Override
    public String toString() {
        return
                "M{" +
                        "br = '" + br + '\'' +
                        ",fid = '" + fid + '\'' +
                        ",size = '" + size + '\'' +
                        ",vd = '" + vd + '\'' +
                        ",sr = '" + sr + '\'' +
                        "}";
    }
}
