package org.api.nmusic.model.vo.playlistallsong;

import lombok.Data;

import java.util.List;

@Data
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
}