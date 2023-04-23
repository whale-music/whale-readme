package org.api.subsonic.model.res.playlists;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class Playlist{
	
	@JacksonXmlProperty(isAttribute = true)
	private int duration;
	
	@JacksonXmlProperty(isAttribute = true)
	private String owner;
	
	@JacksonXmlProperty(isAttribute = true)
	private boolean jsonMemberPublic;
	
	@JacksonXmlProperty(isAttribute = true)
	private String created;
	
	@JacksonXmlProperty(isAttribute = true)
	private String name;
	
	@JacksonXmlProperty(isAttribute = true)
	private String id;
	
	@JacksonXmlProperty(isAttribute = true)
	private String coverArt;
	
	@JacksonXmlProperty(isAttribute = true)
	private int songCount;
	
	@JacksonXmlProperty(isAttribute = true)
	private String changed;
}