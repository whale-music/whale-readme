package org.api.neteasecloudmusic.model.vo.personalfm;

import lombok.Data;

import java.util.List;

@Data
public class DataItem {
    private int no;
    private int copyright;
    private int dayPlays;
    private int fee;
    private Object sign;
    private Object rurl;
    private Privilege privilege;
    private MMusic mMusic;
    private BMusic bMusic;
    private int duration;
    private int score;
    private int rtype;
    private SqMusic sqMusic;
    private boolean starred;
    private List<ArtistsItem> artists;
    private List<Object> rtUrls;
    private int popularity;
    private int playedNum;
    private int hearTime;
    private List<String> alias;
    private int starredNum;
    private Long id;
    private String mp3Url;
    private String alg;
    private Object audition;
    private String transName;
    private Album album;
    private LMusic lMusic;
    private int originCoverType;
    private Object ringtone;
    private String commentThreadId;
    private Object originSongSimpleData;
    private String copyFrom;
    private Object noCopyrightRcmd;
    private Object crbt;
    private int single;
    private Object rtUrl;
    private int ftype;
    private int copyrightId;
    private HMusic hMusic;
    private int mvid;
    private String name;
    private String disc;
    private int position;
    private int mark;
    private int status;
    private Object hrMusic;
    private String reason;
    private String reasonId;
    private String sCtrp;
    private List<String> transNames;
}