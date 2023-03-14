package org.api.neteasecloudmusic.model.vo.personalfm;

import lombok.Data;

@Data
public class BMusic {
    private String extension;
    private int size;
    private int volumeDelta;
    private Object name;
    private int bitrate;
    private int playTime;
    private int id;
    private int dfsId;
    private int sr;
}