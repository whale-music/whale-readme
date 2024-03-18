package org.core.common.constant;

import java.util.List;
import java.util.Map;

public class TargetTagConstant {
    /**
     * 流派
     */
    public static final Byte TARGET_MUSIC_GENRE = 0;
    
    /**
     * 歌曲tag
     */
    public static final Byte TARGET_MUSIC_TAG = 1;
    
    /**
     * 专辑流派
     */
    public static final Byte TARGET_ALBUM_GENRE = 2;
    
    /**
     * 歌单tag
     */
    public static final Byte TARGET_COLLECT_TAG = 3;
    
    /**
     * MV tag
     */
    public static final Byte TARGET_MV_TAG = 4;
    
    public static final List<Byte> TAG = List.of(TARGET_MUSIC_TAG, TARGET_MV_TAG, TARGET_COLLECT_TAG);
    public static final List<Byte> GENERIC = List.of(TARGET_MUSIC_GENRE, TARGET_ALBUM_GENRE);
    public static final Map<String, Byte> keyMap = Map.of(
            "musicTag", TARGET_MUSIC_TAG,
            "musicGenre", TARGET_MUSIC_GENRE,
            "albumGenre", TARGET_ALBUM_GENRE,
            "collectTag", TARGET_COLLECT_TAG,
            "mvTag", TARGET_MV_TAG
    );
    
    public static final Map<Byte, String> valueMap = Map.of(
            TARGET_MUSIC_TAG, "musicTag",
            TARGET_MUSIC_GENRE, "musicGenre",
            TARGET_ALBUM_GENRE, "albumGenre",
            TARGET_COLLECT_TAG, "collectTag",
            TARGET_MV_TAG, "mvTag"
    );
    private TargetTagConstant() {
    }
}
