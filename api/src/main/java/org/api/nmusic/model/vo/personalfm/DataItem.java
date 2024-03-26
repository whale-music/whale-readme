package org.api.nmusic.model.vo.personalfm;

import lombok.Data;

import java.util.List;

@Data
public class DataItem {
    private Integer no;
    private Integer copyright;
    private Integer dayPlays;
    private Integer fee;
    private Object sign;
    private Object rurl;
    private Privilege privilege;
    private MMusic mMusic;
    private BMusic bMusic;
    private Integer duration;
    private Integer score;
    private Integer rtype;
    private SqMusic sqMusic;
    private Boolean starred;
    private List<ArtistsItem> artists;
    private List<Object> rtUrls;
    private Integer popularity;
    private Integer playedNum;
    private Integer hearTime;
    private List<String> alias;
    private Integer starredNum;
    private Long id;
    private String mp3Url;
    private String alg;
    private Object audition;
    private String transName;
    private Album album;
    private LMusic lMusic;
    private Integer originCoverType;
    private Object ringtone;
    private String commentThreadId;
    private Object originSongSimpleData;
    private String copyFrom;
    private Object noCopyrightRcmd;
    private Object crbt;
    private Integer single;
    private Object rtUrl;
    private Integer ftype;
    private Integer copyrightId;
    private HMusic hMusic;
    private Integer mvid;
    private String name;
    private String disc;
    private Integer position;
    private Integer mark;
    private Integer status;
    private Object hrMusic;
    private String reason;
    private String reasonId;
    private String sCtrp;
    private List<String> transNames;
}