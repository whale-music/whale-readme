package org.api.neteasecloudmusic.model.vo.song.lyric;

import lombok.Data;

@Data
public class SongLyricRes {
    private Romalrc romalrc;
    private int code;
    private boolean qfy;
    private Klyric klyric;
    private boolean sfy;
    private Tlyric tlyric;
    private Lrc lrc;
    private boolean sgc;
}