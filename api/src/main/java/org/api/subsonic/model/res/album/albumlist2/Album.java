package org.api.subsonic.model.res.album.albumlist2;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("albumList2")
public class Album{
	
	@JacksonXmlProperty(isAttribute = true)
	private String id;
	
	@JacksonXmlProperty(isAttribute = true)
	private boolean isDir;
	
	@JacksonXmlProperty(isAttribute = true)
	private String parent;
	
	@JacksonXmlProperty(isAttribute = true)
	private String artist;
	
	@JacksonXmlProperty(isAttribute = true)
	private int year;
	
	@JacksonXmlProperty(isAttribute = true)
	private String album;
	
	@JacksonXmlProperty(isAttribute = true)
	private String created;
	
	@JacksonXmlProperty(isAttribute = true)
	private boolean isVideo;
	
	@JacksonXmlProperty(isAttribute = true)
	private String artistId;
	
	@JacksonXmlProperty(isAttribute = true)
	private String coverArt;
	
	@JacksonXmlProperty(isAttribute = true)
	private String title;
	
	@JacksonXmlProperty(isAttribute = true)
	private String played;
	
	@JacksonXmlProperty(isAttribute = true)
	private int songCount;
	
	@JacksonXmlProperty(isAttribute = true)
	private int duration;
	
	@JacksonXmlProperty(isAttribute = true)
	private int playCount;
	
	@JacksonXmlProperty(isAttribute = true)
	private String starred;
	
	@JacksonXmlProperty(isAttribute = true)
	private String name;
	
}