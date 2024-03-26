package org.api.nmusic.model.vo.personalfm;

import lombok.Data;

import java.util.List;

@Data
public class Album {
    private Artist artist;
    private String description;
    private long pic;
    private String type;
    private String picUrl;
    private String briefDesc;
    private List<ArtistsItem> artists;
    private List<Object> alias;
    private boolean onSale;
    private String company;
    private Long id;
    private long picId;
    private Object transName;
    private long publishTime;
    private String picIdStr;
    private String blurPicUrl;
    private String commentThreadId;
    private int gapless;
    private String tags;
    private int companyId;
    private int size;
    private int copyrightId;
    private List<Object> songs;
    private String name;
    private String subType;
    private int mark;
    private int status;
}