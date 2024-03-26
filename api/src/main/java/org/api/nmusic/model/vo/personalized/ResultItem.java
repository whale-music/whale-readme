package org.api.nmusic.model.vo.personalized;

import lombok.Data;

@Data
public class ResultItem {
    private String picUrl;
    private int playCount;
    private int trackCount;
    private boolean canDislike;
    private String name;
    private String copywriter;
    private boolean highQuality;
    private Long id;
    private int type;
    private long trackNumberUpdateTime;
    private String alg;
}