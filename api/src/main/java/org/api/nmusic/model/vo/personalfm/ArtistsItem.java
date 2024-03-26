package org.api.nmusic.model.vo.personalfm;

import lombok.Data;

import java.util.List;

@Data
public class ArtistsItem {
    private String picUrl;
    private String img1v1Url;
    private int topicPerson;
    private String briefDesc;
    private int musicSize;
    private String name;
    private List<Object> alias;
    private int img1v1Id;
    private Long id;
    private int picId;
    private int albumSize;
    private String trans;
}