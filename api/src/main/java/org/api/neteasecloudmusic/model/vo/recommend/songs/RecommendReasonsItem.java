package org.api.neteasecloudmusic.model.vo.recommend.songs;

import lombok.Data;

@Data
public class RecommendReasonsItem {
    private String reason;
    private String reasonId;
    private Long songId;
    private Object targetUrl;
}