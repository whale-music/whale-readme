package org.api.nmusic.model.vo.personalfm;

import lombok.Data;

import java.util.List;

@Data
public class PersonalFMRes {
    private int code;
    private List<DataItem> data;
    private boolean popAdjust;
}