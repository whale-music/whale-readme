package org.api.nmusic.model.vo.songdetail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class PrivilegesItem {
    private Integer flag;
    private String dlLevel;
    private Integer subp;
    private Integer fl;
    private Integer dl;
    private String plLevel;
    private String maxBrLevel;
    private Integer maxbr;
    private Long id;
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
    
    @Schema(name = "0: 免费或无版权, 1: VIP 歌曲, 4: 购买专辑, 8: 非会员可免费播放低音质，会员可播放高音质及下载", description = "fee 为 1 或 8 的歌曲均可单独购买 2 元单曲")
    private Integer fee;
    @Schema(name = "0 为无效歌曲，就是网易云没版权")
    private Integer pl;
}