package org.api.subsonic.model.res.playlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayList {
	
	@JsonProperty("duration")
	@JacksonXmlProperty(isAttribute = true)
	private Integer duration;
	
	@JsonProperty("owner")
	@JacksonXmlProperty(isAttribute = true)
	private String owner;
	
	@JsonProperty("entry")
	@JacksonXmlProperty(isAttribute = true)
	private List<EntryItem> entry;
	
	@JsonProperty("public")
	@JacksonXmlProperty(isAttribute = true)
	private Boolean jsonMemberPublic;
	
	@JsonProperty("created")
	@JacksonXmlProperty(isAttribute = true)
	private String created;
	
	@JsonProperty("name")
	@JacksonXmlProperty(isAttribute = true)
	private String name;
	
	@JsonProperty("id")
	@JacksonXmlProperty(isAttribute = true)
	private String id;
	
	@JsonProperty("coverArt")
	@JacksonXmlProperty(isAttribute = true)
	private String coverArt;
	
	@JsonProperty("songCount")
	@JacksonXmlProperty(isAttribute = true)
	private Integer songCount;
	
	@JsonProperty("changed")
	@JacksonXmlProperty(isAttribute = true)
	private String changed;
}