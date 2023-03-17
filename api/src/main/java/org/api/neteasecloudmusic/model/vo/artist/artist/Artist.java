package org.api.neteasecloudmusic.model.vo.artist.artist;

import lombok.Data;

import java.util.List;

@Data
public class Artist {
    private String img1v1Url;
    private long publishTime;
    private String picIdStr;
    private Long musicSize;
    private String img1v1IdStr;
    private long img1v1Id;
    private int mvSize;
    private boolean followed;
    private int albumSize;
    private String picUrl;
    private int topicPerson;
    private String briefDesc;
    private String name;
    private List<String> alias;
    private Long id;
    private long picId;
    private String trans;
}