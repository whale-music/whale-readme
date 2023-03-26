package org.api.neteasecloudmusic.model.vo.artist.sublist;

import lombok.Data;

import java.util.List;

@Data
public class DataItem {
    private String picUrl;
    private String img1v1Url;
    private String name;
    private List<String> alias;
    private Long id;
    private int mvSize;
    private Integer albumSize;
    private long picId;
    private String info;
    private Object trans;
}