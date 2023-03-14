package org.api.neteasecloudmusic.model.vo.songurl;

import lombok.Data;

@Data
public class DataItem {
    private Integer code;
    private Integer expi;
    private Integer flag;
    private Object effectTypes;
    private Integer fee;
    private Integer urlSource;
    private String type;
    private Boolean canExtend;
    private FreeTimeTrialPrivilege freeTimeTrialPrivilege;
    private Object gain;
    private Integer br;
    private Object uf;
    private String encodeType;
    private Integer rightSource;
    private Long id;
    private Integer payed;
    private Object freeTrialInfo;
    private Object podcastCtrp;
    private String level;
    private FreeTrialPrivilege freeTrialPrivilege;
    private Integer peak;
    private String url;
    private Long size;
    private Integer time;
    private String md5;
}