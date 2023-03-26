package org.api.neteasecloudmusic.model.vo.album.sublist;

import lombok.Data;

import java.util.List;

@Data
public class AlbumSubListRes {
    private Integer code;
    private List<DataItem> data;
    private Integer count;
    private Boolean hasMore;
    private Integer paidCount;
}