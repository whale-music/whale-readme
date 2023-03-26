package org.api.neteasecloudmusic.model.vo.album.album;

import lombok.Data;

import java.util.List;

@Data
public class ArItem{
	private String name;
	private Long id;
	private List<String> alia;
}