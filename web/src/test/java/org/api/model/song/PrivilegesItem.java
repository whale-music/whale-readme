package org.api.model.song;

import java.util.List;

public class PrivilegesItem {
    private Integer flag;
    private String dlLevel;
    private Integer subp;
    private Integer fl;
    private Integer fee;
    private Integer dl;
    private String plLevel;
    private String maxBrLevel;
    private Integer maxbr;
    private Integer id;
    private Integer sp;
    private Integer payed;
    private Object rscl;
    private Integer st;
    private List<ChargeInfoListItem> chargeInfoList;
    private FreeTrialPrivilege freeTrialPrivilege;
    private Integer downloadMaxbr;
    private String downloadMaxBrLevel;
    private Integer cp;
    private Boolean preSell;
    private String playMaxBrLevel;
    private Boolean cs;
    private Boolean toast;
    private Integer playMaxbr;
    private String flLevel;
    private Integer pl;
    
    public Integer getFlag() {
        return flag;
    }
    
    public void setFlag(Integer flag) {
        this.flag = flag;
    }
    
    public String getDlLevel() {
        return dlLevel;
    }
    
    public void setDlLevel(String dlLevel) {
        this.dlLevel = dlLevel;
    }
    
    public Integer getSubp() {
        return subp;
    }
    
    public void setSubp(Integer subp) {
        this.subp = subp;
    }
    
    public Integer getFl() {
        return fl;
    }
    
    public void setFl(Integer fl) {
        this.fl = fl;
    }
    
    public Integer getFee() {
        return fee;
    }
    
    public void setFee(Integer fee) {
        this.fee = fee;
    }
    
    public Integer getDl() {
        return dl;
    }
    
    public void setDl(Integer dl) {
        this.dl = dl;
    }
    
    public String getPlLevel() {
        return plLevel;
    }
    
    public void setPlLevel(String plLevel) {
        this.plLevel = plLevel;
    }
    
    public String getMaxBrLevel() {
        return maxBrLevel;
    }
    
    public void setMaxBrLevel(String maxBrLevel) {
        this.maxBrLevel = maxBrLevel;
    }
    
    public Integer getMaxbr() {
        return maxbr;
    }
    
    public void setMaxbr(Integer maxbr) {
        this.maxbr = maxbr;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getSp() {
        return sp;
    }
    
    public void setSp(Integer sp) {
        this.sp = sp;
    }
    
    public Integer getPayed() {
        return payed;
    }
    
    public void setPayed(Integer payed) {
        this.payed = payed;
    }
    
    public Object getRscl() {
        return rscl;
    }
    
    public void setRscl(Object rscl) {
        this.rscl = rscl;
    }
    
    public Integer getSt() {
        return st;
    }
    
    public void setSt(Integer st) {
        this.st = st;
    }
    
    public List<ChargeInfoListItem> getChargeInfoList() {
        return chargeInfoList;
    }
    
    public void setChargeInfoList(List<ChargeInfoListItem> chargeInfoList) {
        this.chargeInfoList = chargeInfoList;
    }
    
    public FreeTrialPrivilege getFreeTrialPrivilege() {
        return freeTrialPrivilege;
    }
    
    public void setFreeTrialPrivilege(FreeTrialPrivilege freeTrialPrivilege) {
        this.freeTrialPrivilege = freeTrialPrivilege;
    }
    
    public Integer getDownloadMaxbr() {
        return downloadMaxbr;
    }
    
    public void setDownloadMaxbr(Integer downloadMaxbr) {
        this.downloadMaxbr = downloadMaxbr;
    }
    
    public String getDownloadMaxBrLevel() {
        return downloadMaxBrLevel;
    }
    
    public void setDownloadMaxBrLevel(String downloadMaxBrLevel) {
        this.downloadMaxBrLevel = downloadMaxBrLevel;
    }
    
    public Integer getCp() {
        return cp;
    }
    
    public void setCp(Integer cp) {
        this.cp = cp;
    }
    
    public void setPreSell(Boolean preSell) {
        this.preSell = preSell;
    }
    
    public Boolean isPreSell() {
        return preSell;
    }
    
    public String getPlayMaxBrLevel() {
        return playMaxBrLevel;
    }
    
    public void setPlayMaxBrLevel(String playMaxBrLevel) {
        this.playMaxBrLevel = playMaxBrLevel;
    }
    
    public void setCs(Boolean cs) {
        this.cs = cs;
    }
    
    public Boolean isCs() {
        return cs;
    }
    
    public void setToast(Boolean toast) {
        this.toast = toast;
    }
    
    public Boolean isToast() {
        return toast;
    }
    
    public Integer getPlayMaxbr() {
        return playMaxbr;
    }
    
    public void setPlayMaxbr(Integer playMaxbr) {
        this.playMaxbr = playMaxbr;
    }
    
    public String getFlLevel() {
        return flLevel;
    }
    
    public void setFlLevel(String flLevel) {
        this.flLevel = flLevel;
    }
    
    public Integer getPl() {
        return pl;
    }
    
    public void setPl(Integer pl) {
        this.pl = pl;
    }
    
    @Override
    public String toString() {
        return
                "PrivilegesItem{" +
                        "flag = '" + flag + '\'' +
                        ",dlLevel = '" + dlLevel + '\'' +
                        ",subp = '" + subp + '\'' +
                        ",fl = '" + fl + '\'' +
                        ",fee = '" + fee + '\'' +
                        ",dl = '" + dl + '\'' +
                        ",plLevel = '" + plLevel + '\'' +
                        ",maxBrLevel = '" + maxBrLevel + '\'' +
                        ",maxbr = '" + maxbr + '\'' +
                        ",id = '" + id + '\'' +
                        ",sp = '" + sp + '\'' +
                        ",payed = '" + payed + '\'' +
                        ",rscl = '" + rscl + '\'' +
                        ",st = '" + st + '\'' +
                        ",chargeInfoList = '" + chargeInfoList + '\'' +
                        ",freeTrialPrivilege = '" + freeTrialPrivilege + '\'' +
                        ",downloadMaxbr = '" + downloadMaxbr + '\'' +
                        ",downloadMaxBrLevel = '" + downloadMaxBrLevel + '\'' +
                        ",cp = '" + cp + '\'' +
                        ",preSell = '" + preSell + '\'' +
                        ",playMaxBrLevel = '" + playMaxBrLevel + '\'' +
                        ",cs = '" + cs + '\'' +
                        ",toast = '" + toast + '\'' +
                        ",playMaxbr = '" + playMaxbr + '\'' +
                        ",flLevel = '" + flLevel + '\'' +
                        ",pl = '" + pl + '\'' +
                        "}";
    }
}