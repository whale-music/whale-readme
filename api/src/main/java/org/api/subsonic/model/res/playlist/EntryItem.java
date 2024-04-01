package org.api.subsonic.model.res.playlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntryItem {
	
	@JsonProperty("parent")
	@JacksonXmlProperty(isAttribute = true)
	private String parent;
	
	@JsonProperty("artist")
	@JacksonXmlProperty(isAttribute = true)
	private String artist;
	
	@JsonProperty("year")
	@JacksonXmlProperty(isAttribute = true)
	private Integer year;
	
	@JsonProperty("album")
	@JacksonXmlProperty(isAttribute = true)
	private String album;
	
	@JsonProperty("created")
	@JacksonXmlProperty(isAttribute = true)
	private String created;
	
	@JsonProperty("albumId")
	@JacksonXmlProperty(isAttribute = true)
	private String albumId;
	
	@JsonProperty("isVideo")
	@JacksonXmlProperty(isAttribute = true)
	private Boolean isVideo;
	
	@JsonProperty("artistId")
	@JacksonXmlProperty(isAttribute = true)
	private String artistId;
	
	@JsonProperty("coverArt")
	@JacksonXmlProperty(isAttribute = true)
	private String coverArt;
	
	@JsonProperty("title")
	@JacksonXmlProperty(isAttribute = true)
	private String title;
	
	@JsonProperty("suffix")
	@JacksonXmlProperty(isAttribute = true)
	private String suffix;
	
	@JsonProperty("type")
	@JacksonXmlProperty(isAttribute = true)
	private String type;
	
	@JsonProperty("played")
	@JacksonXmlProperty(isAttribute = true)
	private String played;
	
	@JsonProperty("duration")
	@JacksonXmlProperty(isAttribute = true)
	private Integer duration;
	
	@JsonProperty("path")
	@JacksonXmlProperty(isAttribute = true)
	private String path;
	
	@JsonProperty("playCount")
	@JacksonXmlProperty(isAttribute = true)
	private Integer playCount;
	
	@JsonProperty("size")
	@JacksonXmlProperty(isAttribute = true)
	private Integer size;
	
	@JsonProperty("bitRate")
	@JacksonXmlProperty(isAttribute = true)
	private Integer bitRate;
	
	@JsonProperty("genre")
	@JacksonXmlProperty(isAttribute = true)
	private String genre;
	
	@JsonProperty("id")
	@JacksonXmlProperty(isAttribute = true)
	private String id;
	
	@JsonProperty("track")
	@JacksonXmlProperty(isAttribute = true)
	private Integer track;
	
	@JsonProperty("contentType")
	@JacksonXmlProperty(isAttribute = true)
	private String contentType;
	
	@JsonProperty("isDir")
	@JacksonXmlProperty(isAttribute = true)
	private Boolean isDir;
	
	@JsonProperty("userRating")
	@JacksonXmlProperty(isAttribute = true)
	private Integer userRating;
	
	@JsonProperty("starred")
	@JacksonXmlProperty(isAttribute = true)
	private String starred;
}