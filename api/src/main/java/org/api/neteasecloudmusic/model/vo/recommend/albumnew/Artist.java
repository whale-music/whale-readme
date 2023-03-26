package org.api.neteasecloudmusic.model.vo.recommend.albumnew;

import lombok.Data;

import java.util.List;

@Data
public class Artist {
    private String img1v1Url;
    private Integer musicSize;
    private String img1v1IdStr;
    private Long img1v1Id;
    private boolean followed;
    private Integer albumSize;
    private String picUrl;
    private Integer topicPerson;
    private String briefDesc;
    private String name;
    private List<Object> alias;
    private Integer id;
    private Integer picId;
    private String trans;
}