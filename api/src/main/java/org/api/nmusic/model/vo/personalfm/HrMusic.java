package org.api.nmusic.model.vo.personalfm;

import lombok.Data;

@Data
public class HrMusic {
    private String extension;
    private int size;
    private int volumeDelta;
    private Object name;
    private int bitrate;
    private int playTime;
    private long id;
    private int dfsId;
    private int sr;
}