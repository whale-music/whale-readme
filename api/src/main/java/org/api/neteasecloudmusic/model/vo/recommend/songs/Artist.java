package org.api.neteasecloudmusic.model.vo.recommend.songs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Artist {
    private String name;
    private Long id;
    private Long picId;
    private Long img1v1Id;
    private String briefDesc;
    private String picUrl;
    private String img1v1Url;
    private Integer albumSize;
    private List<String> alias;
    private String trans;
    private Long musicSize;
    private Long topicPerson;
}
