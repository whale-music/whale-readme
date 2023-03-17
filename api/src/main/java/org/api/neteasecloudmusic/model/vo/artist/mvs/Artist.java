package org.api.neteasecloudmusic.model.vo.artist.mvs;

import lombok.Data;

import java.util.List;

@Data
public class Artist {
    private String img1v1Url;
    private int musicSize;
    private String img1v1IdStr;
    private long img1v1Id;
    private int albumSize;
    private String picUrl;
    private int topicPerson;
    private String briefDesc;
    private String name;
    private List<Object> alias;
    private int id;
    private int picId;
    private String trans;
}