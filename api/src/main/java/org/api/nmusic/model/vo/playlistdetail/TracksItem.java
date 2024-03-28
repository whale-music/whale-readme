package org.api.nmusic.model.vo.playlistdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TracksItem {
	
	@JsonProperty("no")
	private Integer no;
	
	@JsonProperty("rt")
	private String rt;
	
	@JsonProperty("copyright")
	private Integer copyright;
	
	@JsonProperty("fee")
	private Integer fee;
	
	@JsonProperty("rurl")
	private Object rurl;
	
	@JsonProperty("hr")
	private Object hr;
	
	@JsonProperty("tagPicList")
	private Object tagPicList;
	
	@JsonProperty("mst")
	private Integer mst;
	
	@JsonProperty("pst")
	private Integer pst;
	
	@JsonProperty("pop")
	private Integer pop;
	
	@JsonProperty("dt")
	private Integer dt;
	
	@JsonProperty("awardTags")
	private Object awardTags;
	
	@JsonProperty("rtype")
	private Integer rtype;
	
	@JsonProperty("s_id")
	private Integer sId;
	
	@JsonProperty("rtUrls")
	private List<Object> rtUrls;
	
	@JsonProperty("resourceState")
	private Boolean resourceState;
	
	@JsonProperty("songJumpInfo")
	private Object songJumpInfo;
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("entertainmentTags")
	private Object entertainmentTags;
	
	@JsonProperty("sq")
	private Sq sq;
	
	@JsonProperty("st")
	private Integer st;
	
	@JsonProperty("a")
	private Object a;
	
	@JsonProperty("cd")
	private String cd;
	
	@JsonProperty("publishTime")
	private Long publishTime;
	
	@JsonProperty("cf")
	private String cf;
	
	@JsonProperty("originCoverType")
	private Integer originCoverType;
	
	@JsonProperty("h")
	private H h;
	
	@JsonProperty("mv")
	private Integer mv;
	
	@JsonProperty("al")
	private Al al;
	
	@JsonProperty("originSongSimpleData")
	private OriginSongSimpleData originSongSimpleData;
	
	@JsonProperty("l")
	private L l;
	
	@JsonProperty("m")
	private M m;
	
	@JsonProperty("version")
	private Integer version;
	
	@JsonProperty("cp")
	private Integer cp;
	
	@JsonProperty("alia")
	private List<String> alia;
	
	@JsonProperty("djId")
	private Integer djId;
	
	@JsonProperty("noCopyrightRcmd")
	private Object noCopyrightRcmd;
	
	@JsonProperty("crbt")
	private Object crbt;
	
	@JsonProperty("single")
	private Integer single;
	
	@JsonProperty("ar")
	private List<ArItem> ar;
	
	@JsonProperty("rtUrl")
	private Object rtUrl;
	
	@JsonProperty("ftype")
	private Integer ftype;
	
	@JsonProperty("t")
	private Integer t;
	
	@JsonProperty("v")
	private Integer v;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("tns")
	private List<String> tns;
	
	@JsonProperty("mark")
	private Integer mark;
}
