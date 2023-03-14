package org.api.neteasecloudmusic.model.vo.personalfm;

import lombok.Data;

import java.util.List;

@Data
public class Artist {
    private String picUrl;
    private String img1v1Url;
    private int topicPerson;
    private String briefDesc;
    private int musicSize;
    private String name;
    private List<Object> alias;
    private int img1v1Id;
    private int id;
    private int picId;
    private int albumSize;
    private String trans;
}