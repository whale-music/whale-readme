package org.api.neteasecloudmusic.model.vo.recommend.songs;

import lombok.Data;

import java.util.List;

@Data
public class RecommendSongerRes {
    private List<RecommendReasonsItem> recommendReasons;
    private List<Object> orderSongs;
    private List<DailySongsItem> dailySongs;
    private Object mvResourceInfos;
}