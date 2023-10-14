package org.api.neteasecloudmusic.model.vo.artist.artist;

import lombok.Data;

import java.util.List;

@Data
public class Artist {
    private String img1v1Url;
    private Long publishTime;
    private String picIdStr;
    private Long musicSize;
    private String img1v1IdStr;
    private Long img1v1Id;
    private Integer mvSize;
    private Boolean followed;
    private Integer albumSize;
    private String picUrl;
    private Integer topicPerson;
    private String briefDesc;
    private String name;
    private List<String> alias;
    private Long id;
    private Long picId;
    private String trans;
}