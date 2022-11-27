package org.musicbox.common.vo.neteasecloudmusic.playlistallsong;

import lombok.Data;

import java.util.List;

@Data
public class ArItem {
    private String name;
    private List<Object> tns;
    private List<Object> alias;
    private int id;
}