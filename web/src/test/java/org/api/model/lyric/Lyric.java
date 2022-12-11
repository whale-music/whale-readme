package org.api.model.lyric;

public class Lyric {
    private Romalrc romalrc;
    private int code;
    private boolean qfy;
    private Klyric klyric;
    private boolean sfy;
    private Tlyric tlyric;
    private Lrc lrc;
    private boolean sgc;
    
    public Romalrc getRomalrc() {
        return romalrc;
    }
    
    public void setRomalrc(Romalrc romalrc) {
        this.romalrc = romalrc;
    }
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public boolean isQfy() {
        return qfy;
    }
    
    public void setQfy(boolean qfy) {
        this.qfy = qfy;
    }
    
    public Klyric getKlyric() {
        return klyric;
    }
    
    public void setKlyric(Klyric klyric) {
        this.klyric = klyric;
    }
    
    public boolean isSfy() {
        return sfy;
    }
    
    public void setSfy(boolean sfy) {
        this.sfy = sfy;
    }
    
    public Tlyric getTlyric() {
        return tlyric;
    }
    
    public void setTlyric(Tlyric tlyric) {
        this.tlyric = tlyric;
    }
    
    public Lrc getLrc() {
        return lrc;
    }
    
    public void setLrc(Lrc lrc) {
        this.lrc = lrc;
    }
    
    public boolean isSgc() {
        return sgc;
    }
    
    public void setSgc(boolean sgc) {
        this.sgc = sgc;
    }
}
