package org.core.common.constant;

import java.util.Map;

/**
 * 播放类型可能是音乐，歌单，专辑,0为音乐, 1为专辑, 2为歌手, 3为歌单, 4mv
 */
public class HistoryConstant {
    public static final Byte MUSIC = 0;
    public static final Byte ALBUM = 1;
    public static final Byte ARTIST = 2;
    public static final Byte PLAYLIST = 3;
    public static final Byte MV = 4;
    
    public static final Map<Byte, String> map =
            Map.of(
                    MUSIC, "music",
                    ALBUM, "album",
                    ARTIST, "artist",
                    PLAYLIST, "playlist",
                    MV, "mv"
            );
    
    private HistoryConstant() {
    }
}
