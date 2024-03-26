package org.api.nmusic.model.vo.songurl;

import lombok.Data;

import java.util.List;

@Data
public class SongUrlRes {
    private int code;
    private List<DataItem> data;
}