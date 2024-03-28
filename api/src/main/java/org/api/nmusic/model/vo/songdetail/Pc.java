package org.api.nmusic.model.vo.songdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Pc {
    
    @JsonProperty("br")
    private Integer br;
    
    @JsonProperty("ar")
    private String ar;
    
    @JsonProperty("uid")
    private Integer uid;
    
    @JsonProperty("alb")
    private String alb;
    
    @JsonProperty("nickname")
    private String nickname;
    
    @JsonProperty("fn")
    private String fn;
    
    @JsonProperty("sn")
    private String sn;
    
    @JsonProperty("cid")
    private String cid;
}
