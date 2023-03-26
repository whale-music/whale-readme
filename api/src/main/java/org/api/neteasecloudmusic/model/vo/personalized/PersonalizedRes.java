package org.api.neteasecloudmusic.model.vo.personalized;

import lombok.Data;

import java.util.List;

@Data
public class PersonalizedRes {
    private List<ResultItem> result;
    private int code;
    private boolean hasTaste;
    private int category;
}