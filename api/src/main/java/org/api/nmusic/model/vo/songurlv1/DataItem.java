package org.api.nmusic.model.vo.songurlv1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataItem {
    
    @JsonProperty("code")
    private Integer code;
    
    @Schema(name = "音乐链接的有效期，以秒为单位")
    @JsonProperty("expi")
    private Integer expi;
    
    @JsonProperty("flag")
    private Integer flag;
    
    @JsonProperty("effectTypes")
    private Object effectTypes;
    
    @Schema(name = "0: 免费或无版权, 1: VIP 歌曲, 4: 购买专辑, 8: 非会员可免费播放低音质，会员可播放高音质及下载", description = "fee 为 1 或 8 的歌曲均可单独购买 2 元单曲")
    @JsonProperty("fee")
    private Integer fee;
    
    @JsonProperty("urlSource")
    private Integer urlSource;
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("canExtend")
    private Boolean canExtend;
    
    @JsonProperty("freeTimeTrialPrivilege")
    private FreeTimeTrialPrivilege freeTimeTrialPrivilege;
    
    @JsonProperty("gain")
    private Object gain;
    
    @JsonProperty("br")
    private Integer br;
    
    @JsonProperty("uf")
    private Object uf;
    
    @JsonProperty("encodeType")
    private String encodeType;
    
    @JsonProperty("rightSource")
    private Integer rightSource;
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("payed")
    private Integer payed;
    
    @JsonProperty("freeTrialInfo")
    private FreeTrialInfo freeTrialInfo;
    
    @JsonProperty("channelLayout")
    private Object channelLayout;
    
    @JsonProperty("podcastCtrp")
    private Object podcastCtrp;
    
    @JsonProperty("level")
    private String level;
    
    @JsonProperty("freeTrialPrivilege")
    private FreeTrialPrivilege freeTrialPrivilege;
    
    @JsonProperty("peak")
    private Integer peak;
    
    @JsonProperty("message")
    private Object message;
    
    @JsonProperty("url")
    private String url;
    
    @JsonProperty("size")
    private Long size;
    
    @JsonProperty("time")
    private Integer time;
    
    @JsonProperty("md5")
    private String md5;
}
