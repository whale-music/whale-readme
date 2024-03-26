package org.api.nmusic.model.vo.album.sublist;

import lombok.Data;

import java.util.List;

@Data
public class DataItem {
    private List<Object> msg;
    private String picUrl;
    private Integer size;
    private List<ArtistsItem> artists;
    private List<Object> transNames;
    private String name;
    private List<String> alias;
    private Long id;
    private Long subTime;
    private Long picId;
}