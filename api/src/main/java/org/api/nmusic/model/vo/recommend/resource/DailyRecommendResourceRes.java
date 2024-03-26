package org.api.nmusic.model.vo.recommend.resource;

import lombok.Data;

@Data
public class DailyRecommendResourceRes {
    private String picUrl;
    private Creator creator;
    private int trackCount;
    private Long createTime;
    private long playcount;
    private String name;
    private String copywriter;
    private long id;
    private int type;
    private Long userId;
    private String alg;
}