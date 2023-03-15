package org.api.neteasecloudmusic.model.vo.recommend.albumnew;

import lombok.Data;

import java.util.List;

@Data
public class RecommendAlbumNewRes {
    private Artist artist;
    private String description;
    private Long pic;
    private String type;
    private Object awardTags;
    private String picUrl;
    private List<ArtistsItem> artists;
    private String briefDesc;
    private boolean onSale;
    private List<Object> alias;
    private String company;
    private Long id;
    private Long picId;
    private Long publishTime;
    private String picIdStr;
    private String blurPicUrl;
    private String commentThreadId;
    private String tags;
    private Integer companyId;
    private Integer size;
    private Integer copyrightId;
    private List<Object> songs;
    private boolean paid;
    private String name;
    private String subType;
    private Integer mark;
    private Integer status;
}