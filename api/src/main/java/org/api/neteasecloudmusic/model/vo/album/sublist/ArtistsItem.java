package org.api.neteasecloudmusic.model.vo.album.sublist;

import lombok.Data;

import java.util.List;

@Data
public class ArtistsItem {
    private String img1v1Url;
    private Integer musicSize;
    private String img1v1IdStr;
    private long img1v1Id;
    private Boolean followed;
    private Integer albumSize;
    private String picUrl;
    private Integer topicPerson;
    private String briefDesc;
    private String name;
    private List<String> alias;
    private Long id;
    private Integer picId;
    private String trans;
}