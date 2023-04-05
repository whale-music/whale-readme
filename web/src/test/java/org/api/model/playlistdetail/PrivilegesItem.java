package org.api.model.playlistdetail;

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
    private boolean paidBigBang;
    private String maxBrLevel;
    private Integer maxbr;
    private Integer id;
    private Integer sp;
    private Integer payed;
    private Object rscl;
    private Integer st;
    private Integer realPayed;
    private List<ChargeInfoListItem> chargeInfoList;
    private FreeTrialPrivilege freeTrialPrivilege;
    private Integer downloadMaxbr;
    private String downloadMaxBrLevel;
    private Integer cp;
    private boolean preSell;
    private String playMaxBrLevel;
    private boolean cs;
    private boolean toast;
    private Integer playMaxbr;
    private Object pc;
    private String flLevel;
    private Integer pl;
}