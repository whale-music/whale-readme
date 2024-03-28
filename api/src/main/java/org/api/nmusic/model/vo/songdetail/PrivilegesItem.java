package org.api.nmusic.model.vo.songdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PrivilegesItem {
	
	@JsonProperty("flag")
	private Integer flag;
	
	@JsonProperty("dlLevel")
	private String dlLevel;
	
	@JsonProperty("subp")
	private Integer subp;
	
	@JsonProperty("fl")
	private Integer fl;
	
	@Schema(name = "0: 免费或无版权, 1: VIP 歌曲, 4: 购买专辑, 8: 非会员可免费播放低音质，会员可播放高音质及下载", description = "fee 为 1 或 8 的歌曲均可单独购买 2 元单曲")
	@JsonProperty("fee")
	private Integer fee;
	
	@JsonProperty("dl")
	private Integer dl;
	
	@JsonProperty("plLevel")
	private String plLevel;
	
	@JsonProperty("maxBrLevel")
	private String maxBrLevel;
	
	@JsonProperty("rightSource")
	private Integer rightSource;
	
	@JsonProperty("maxbr")
	private Integer maxbr;
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("sp")
	private Integer sp;
	
	@JsonProperty("payed")
	private Integer payed;
	
	@JsonProperty("rscl")
	private Object rscl;
	
	@JsonProperty("st")
	private Integer st;
	
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
	
	@JsonProperty("flLevel")
	private String flLevel;
	
	@Schema(name = "0 为无效歌曲，就是网易云没版权")
	@JsonProperty("pl")
	private Integer pl;
}
