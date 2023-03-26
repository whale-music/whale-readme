package org.api.model.album;

import java.util.List;

public class Privilege{
	private int flag;
	private String dlLevel;
	private int subp;
	private int fl;
	private int fee;
	private int dl;
	private String plLevel;
	private String maxBrLevel;
	private int maxbr;
	private int id;
	private int sp;
	private int payed;
	private Object rscl;
	private int st;
	private List<ChargeInfoListItem> chargeInfoList;
	private FreeTrialPrivilege freeTrialPrivilege;
	private int downloadMaxbr;
	private String downloadMaxBrLevel;
	private int cp;
	private boolean preSell;
	private String playMaxBrLevel;
	private boolean cs;
	private boolean toast;
	private int playMaxbr;
	private String flLevel;
	private int pl;

	public int getFlag(){
		return flag;
	}

	public void setFlag(int flag){
		this.flag = flag;
	}

	public String getDlLevel(){
		return dlLevel;
	}

	public void setDlLevel(String dlLevel){
		this.dlLevel = dlLevel;
	}

	public int getSubp(){
		return subp;
	}

	public void setSubp(int subp){
		this.subp = subp;
	}

	public int getFl(){
		return fl;
	}

	public void setFl(int fl){
		this.fl = fl;
	}

	public int getFee(){
		return fee;
	}

	public void setFee(int fee){
		this.fee = fee;
	}

	public int getDl(){
		return dl;
	}

	public void setDl(int dl){
		this.dl = dl;
	}

	public String getPlLevel(){
		return plLevel;
	}

	public void setPlLevel(String plLevel){
		this.plLevel = plLevel;
	}

	public String getMaxBrLevel(){
		return maxBrLevel;
	}

	public void setMaxBrLevel(String maxBrLevel){
		this.maxBrLevel = maxBrLevel;
	}

	public int getMaxbr(){
		return maxbr;
	}

	public void setMaxbr(int maxbr){
		this.maxbr = maxbr;
	}

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getSp(){
		return sp;
	}

	public void setSp(int sp){
		this.sp = sp;
	}

	public int getPayed(){
		return payed;
	}

	public void setPayed(int payed){
		this.payed = payed;
	}

	public Object getRscl(){
		return rscl;
	}

	public void setRscl(Object rscl){
		this.rscl = rscl;
	}

	public int getSt(){
		return st;
	}

	public void setSt(int st){
		this.st = st;
	}

	public List<ChargeInfoListItem> getChargeInfoList(){
		return chargeInfoList;
	}

	public void setChargeInfoList(List<ChargeInfoListItem> chargeInfoList){
		this.chargeInfoList = chargeInfoList;
	}

	public FreeTrialPrivilege getFreeTrialPrivilege(){
		return freeTrialPrivilege;
	}

	public void setFreeTrialPrivilege(FreeTrialPrivilege freeTrialPrivilege){
		this.freeTrialPrivilege = freeTrialPrivilege;
	}

	public int getDownloadMaxbr(){
		return downloadMaxbr;
	}

	public void setDownloadMaxbr(int downloadMaxbr){
		this.downloadMaxbr = downloadMaxbr;
	}

	public String getDownloadMaxBrLevel(){
		return downloadMaxBrLevel;
	}

	public void setDownloadMaxBrLevel(String downloadMaxBrLevel){
		this.downloadMaxBrLevel = downloadMaxBrLevel;
	}

	public int getCp(){
		return cp;
	}

	public void setCp(int cp){
		this.cp = cp;
	}

	public boolean isPreSell(){
		return preSell;
	}

	public void setPreSell(boolean preSell){
		this.preSell = preSell;
	}

	public String getPlayMaxBrLevel(){
		return playMaxBrLevel;
	}

	public void setPlayMaxBrLevel(String playMaxBrLevel){
		this.playMaxBrLevel = playMaxBrLevel;
	}

	public boolean isCs(){
		return cs;
	}

	public void setCs(boolean cs){
		this.cs = cs;
	}

	public boolean isToast(){
		return toast;
	}

	public void setToast(boolean toast){
		this.toast = toast;
	}

	public int getPlayMaxbr(){
		return playMaxbr;
	}

	public void setPlayMaxbr(int playMaxbr){
		this.playMaxbr = playMaxbr;
	}

	public String getFlLevel(){
		return flLevel;
	}

	public void setFlLevel(String flLevel){
		this.flLevel = flLevel;
	}

	public int getPl(){
		return pl;
	}

	public void setPl(int pl){
		this.pl = pl;
	}

	@Override
 	public String toString(){
		return 
			"Privilege{" + 
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