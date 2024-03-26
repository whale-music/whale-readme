package org.api.nmusic.model.vo.playlistdetail;

import lombok.Data;

import java.util.List;

@Data
public class ArItem {
    private String name;
    private List<Object> tns;
    private List<String> alias;
    private Long id;
}