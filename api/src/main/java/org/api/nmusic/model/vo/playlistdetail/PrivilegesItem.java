package org.api.nmusic.model.vo.playlistdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PrivilegesItem {
	
	@JsonProperty("flag")
	private Integer flag;
	
	@JsonProperty("dlLevel")
	private String dlLevel;
	
	@JsonProperty("subp")
	private Integer subp;
	
	@JsonProperty("fl")
	private Integer fl;
	
	@JsonProperty("fee")
	private Integer fee;
	
	@JsonProperty("dl")
	private Integer dl;
	
	@JsonProperty("plLevel")
	private String plLevel;
	
	@JsonProperty("paidBigBang")
	private Boolean paidBigBang;
	
	@JsonProperty("maxBrLevel")
	private String maxBrLevel;
	
	@JsonProperty("rightSource")
	private Integer rightSource;
	
	@JsonProperty("maxbr")
	private Integer maxbr;
	
	@JsonProperty("id")
	private Integer id;
	
	@JsonProperty("sp")
	private Integer sp;
	
	@JsonProperty("payed")
	private Integer payed;
	
	@JsonProperty("rscl")
	private Object rscl;
	
	@JsonProperty("st")
	private Integer st;
	
	@JsonProperty("realPayed")
	private Integer realPayed;
	
	@JsonProperty("chargeInfoList")
	private List<ChargeInfoListItem> chargeInfoList;
	
	@JsonProperty("freeTrialPrivilege")
	private FreeTrialPrivilege freeTrialPrivilege;
	
	@JsonProperty("downloadMaxbr")
	private Integer downloadMaxbr;
	
	@JsonProperty("downloadMaxBrLevel")
	private String downloadMaxBrLevel;
	
	@JsonProperty("cp")
	private Integer cp;
	
	@JsonProperty("preSell")
	private Boolean preSell;
	
	@JsonProperty("playMaxBrLevel")
	private String playMaxBrLevel;
	
	@JsonProperty("cs")
	private Boolean cs;
	
	@JsonProperty("toast")
	private Boolean toast;
	
	@JsonProperty("playMaxbr")
	private Integer playMaxbr;
	
	@JsonProperty("pc")
	private Object pc;
	
	@JsonProperty("flLevel")
	private String flLevel;
	
	@JsonProperty("pl")
	private Integer pl;
}
