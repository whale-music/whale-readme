package org.api.nmusic.model.vo.artist.sublist;

import lombok.Data;

import java.util.List;

@Data
public class ArtistSubListRes {
    private int code;
    private List<DataItem> data;
    private boolean hasMore;
    private int count;
}