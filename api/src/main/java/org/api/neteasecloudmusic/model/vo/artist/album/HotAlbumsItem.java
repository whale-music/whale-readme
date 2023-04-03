package org.api.neteasecloudmusic.model.vo.artist.album;

import lombok.Data;

import java.util.List;

@Data
public class HotAlbumsItem {
    private Artist artist;
    private String description;
    private Long pic;
    private boolean isSub;
    private String type;
    private Object awardTags;
    private String picUrl;
    private List<ArtistsItem> artists;
    private String briefDesc;
    private boolean onSale;
    private List<String> alias;
    private String company;
    private Long id;
    private long picId;
    private long publishTime;
    private String picIdStr;
    private String blurPicUrl;
    private String commentThreadId;
    private String tags;
    private int companyId;
    private int size;
    private int copyrightId;
    private List<Object> songs;
    private boolean paid;
    private String name;
    private String subType;
    private int mark;
    private int status;
}