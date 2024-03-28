package org.api.nmusic.model.vo.cloudsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CloudSearchRes {
    
    @JsonProperty("result")
    private Result result;
    
    @JsonProperty("code")
    private int code;
    
    @Getter
    @Setter
    public static class Result {
        
        @JsonProperty("searchQcReminder")
        private Object searchQcReminder;
        
        @JsonProperty("songs")
        private List<SongsItem> songs;
        
        @JsonProperty("songCount")
        private Long songCount;
        
        
        @Getter
        @Setter
        public static class SongsItem {
            
            @JsonProperty("no")
            private int no;
            
            @JsonProperty("rt")
            private String rt;
            
            @JsonProperty("copyright")
            private int copyright;
            
            @JsonProperty("fee")
            private int fee;
            
            @JsonProperty("rurl")
            private Object rurl;
            
            @JsonProperty("hr")
            private Object hr;
            
            @JsonProperty("tagPicList")
            private Object tagPicList;
            
            @JsonProperty("privilege")
            private Privilege privilege;
            
            @JsonProperty("mst")
            private int mst;
            
            @JsonProperty("pst")
            private int pst;
            
            @JsonProperty("pop")
            private int pop;
            
            @JsonProperty("dt")
            private int dt;
            
            @JsonProperty("rtype")
            private int rtype;
            
            @JsonProperty("s_id")
            private int sId;
            
            @JsonProperty("rtUrls")
            private List<Object> rtUrls;
            
            @JsonProperty("resourceState")
            private boolean resourceState;
            
            @JsonProperty("songJumpInfo")
            private Object songJumpInfo;
            
            @JsonProperty("id")
            private Long id;
            
            @JsonProperty("entertainmentTags")
            private Object entertainmentTags;
            
            @JsonProperty("sq")
            private Sq sq;
            
            @JsonProperty("st")
            private int st;
            
            @JsonProperty("a")
            private Object a;
            
            @JsonProperty("cd")
            private String cd;
            
            @JsonProperty("publishTime")
            private long publishTime;
            
            @JsonProperty("cf")
            private String cf;
            
            @JsonProperty("originCoverType")
            private int originCoverType;
            
            @JsonProperty("h")
            private H h;
            
            @JsonProperty("mv")
            private int mv;
            
            @JsonProperty("al")
            private Al al;
            
            @JsonProperty("originSongSimpleData")
            private OriginSongSimpleData originSongSimpleData;
            
            @JsonProperty("l")
            private L l;
            
            @JsonProperty("m")
            private M m;
            
            @JsonProperty("version")
            private int version;
            
            @JsonProperty("cp")
            private int cp;
            
            @JsonProperty("alia")
            private List<Object> alia;
            
            @JsonProperty("djId")
            private int djId;
            
            @JsonProperty("noCopyrightRcmd")
            private Object noCopyrightRcmd;
            
            @JsonProperty("crbt")
            private Object crbt;
            
            @JsonProperty("single")
            private int single;
            
            @JsonProperty("ar")
            private List<ArItem> ar;
            
            @JsonProperty("rtUrl")
            private Object rtUrl;
            
            @JsonProperty("ftype")
            private int ftype;
            
            @JsonProperty("t")
            private int t;
            
            @JsonProperty("v")
            private int v;
            
            @JsonProperty("name")
            private String name;
            
            @JsonProperty("mark")
            private long mark;
            
            @JsonProperty("tns")
            private List<String> tns;
            
            @Getter
            @Setter
            @AllArgsConstructor
            public static class M {
                
                @JsonProperty("br")
                private int br;
                
                @JsonProperty("fid")
                private int fid;
                
                @JsonProperty("size")
                private int size;
                
                @JsonProperty("vd")
                private int vd;
                
                @JsonProperty("sr")
                private int sr;
            }
            
            @Getter
            @Setter
            @AllArgsConstructor
            public static class Sq {
                
                @JsonProperty("br")
                private int br;
                
                @JsonProperty("fid")
                private int fid;
                
                @JsonProperty("size")
                private int size;
                
                @JsonProperty("vd")
                private int vd;
                
                @JsonProperty("sr")
                private int sr;
            }
            
            
            @Getter
            @Setter
            @AllArgsConstructor
            public static class L {
                
                @JsonProperty("br")
                private int br;
                
                @JsonProperty("fid")
                private int fid;
                
                @JsonProperty("size")
                private int size;
                
                @JsonProperty("vd")
                private int vd;
                
                @JsonProperty("sr")
                private int sr;
            }
            
            
            @Getter
            @Setter
            @AllArgsConstructor
            public static class Hr {
                
                @JsonProperty("br")
                private int br;
                
                @JsonProperty("fid")
                private int fid;
                
                @JsonProperty("size")
                private int size;
                
                @JsonProperty("vd")
                private int vd;
                
                @JsonProperty("sr")
                private int sr;
            }
            
            
            @Getter
            @Setter
            @AllArgsConstructor
            public static class H {
                
                @JsonProperty("br")
                private int br;
                
                @JsonProperty("fid")
                private int fid;
                
                @JsonProperty("size")
                private int size;
                
                @JsonProperty("vd")
                private int vd;
                
                @JsonProperty("sr")
                private int sr;
            }
            
            
            @Getter
            @Setter
            public static class Al {
                
                @JsonProperty("picUrl")
                private String picUrl;
                
                @JsonProperty("name")
                private String name;
                
                @JsonProperty("tns")
                private List<Object> tns;
                
                @JsonProperty("pic_str")
                private String picStr;
                
                @JsonProperty("id")
                private Long id;
                
                @JsonProperty("pic")
                private long pic;
            }
            
            
            @Getter
            @Setter
            public static class ArItem {
                
                @JsonProperty("name")
                private String name;
                
                @Schema(name = "可能是别名")
                @JsonProperty("tns")
                private List<String> tns;
                
                @JsonProperty("alias")
                private List<String> alias;
                
                @JsonProperty("id")
                private Long id;
                
                @JsonProperty("alia")
                private List<String> alia;
            }
            
            @Getter
            @Setter
            public static class OriginSongSimpleData {
                
                @JsonProperty("artists")
                private List<ArtistsItem> artists;
                
                @JsonProperty("name")
                private String name;
                
                @JsonProperty("songId")
                private int songId;
                
                @JsonProperty("albumMeta")
                private AlbumMeta albumMeta;
                
                @Getter
                @Setter
                public static class AlbumMeta {
                    
                    @JsonProperty("name")
                    private String name;
                    
                    @JsonProperty("id")
                    private int id;
                }
                
                
                @Getter
                @Setter
                public static class ArtistsItem {
                    
                    @JsonProperty("name")
                    private String name;
                    
                    @JsonProperty("id")
                    private int id;
                }
                
            }
            
            
            @Getter
            @Setter
            public static class Privilege {
                
                @JsonProperty("flag")
                private int flag;
                
                @JsonProperty("dlLevel")
                private String dlLevel;
                
                @JsonProperty("subp")
                private int subp;
                
                @JsonProperty("fl")
                private int fl;
                
                @JsonProperty("fee")
                private int fee;
                
                @JsonProperty("dl")
                private int dl;
                
                @JsonProperty("plLevel")
                private String plLevel;
                
                @JsonProperty("maxBrLevel")
                private String maxBrLevel;
                
                @JsonProperty("rightSource")
                private int rightSource;
                
                @JsonProperty("maxbr")
                private int maxbr;
                
                @JsonProperty("id")
                private int id;
                
                @JsonProperty("sp")
                private int sp;
                
                @JsonProperty("payed")
                private int payed;
                
                @JsonProperty("rscl")
                private Object rscl;
                
                @JsonProperty("st")
                private int st;
                
                @JsonProperty("chargeInfoList")
                private List<ChargeInfoListItem> chargeInfoList;
                
                @JsonProperty("freeTrialPrivilege")
                private FreeTrialPrivilege freeTrialPrivilege;
                
                @JsonProperty("downloadMaxbr")
                private int downloadMaxbr;
                
                @JsonProperty("downloadMaxBrLevel")
                private String downloadMaxBrLevel;
                
                @JsonProperty("cp")
                private int cp;
                
                @JsonProperty("preSell")
                private boolean preSell;
                
                @JsonProperty("playMaxBrLevel")
                private String playMaxBrLevel;
                
                @JsonProperty("cs")
                private boolean cs;
                
                @JsonProperty("toast")
                private boolean toast;
                
                @JsonProperty("playMaxbr")
                private int playMaxbr;
                
                @JsonProperty("flLevel")
                private String flLevel;
                
                @JsonProperty("pl")
                private int pl;
                
                @Getter
                @Setter
                public static class ChargeInfoListItem {
                    
                    @JsonProperty("rate")
                    private int rate;
                    
                    @JsonProperty("chargeMessage")
                    private Object chargeMessage;
                    
                    @JsonProperty("chargeType")
                    private int chargeType;
                    
                    @JsonProperty("chargeUrl")
                    private Object chargeUrl;
                }
                
                
                @Getter
                @Setter
                @AllArgsConstructor
                public static class FreeTrialPrivilege {
                    
                    @JsonProperty("userConsumable")
                    private boolean userConsumable;
                    
                    @JsonProperty("resConsumable")
                    private boolean resConsumable;
                    
                    @JsonProperty("cannotListenReason")
                    private int cannotListenReason;
                    
                    @JsonProperty("listenType")
                    private int listenType;
                }
            }
            
        }
        
    }
    
}
