package org.core.common.constant;

import java.util.Map;

public class PicTypeConstant {
    
    /**
     * 歌曲
     */
    public static final byte MUSIC = 0;
    
    /**
     * 歌单
     */
    public static final byte PLAYLIST = 1;
    
    /**
     * 专辑
     */
    public static final byte ALBUM = 2;
    
    /**
     * 歌手
     */
    public static final byte ARTIST = 3;
    
    /**
     * 用户头像
     */
    public static final byte USER_AVATAR = 4;
    
    /**
     * 用户背景
     */
    public static final byte USER_BACKGROUND = 5;
    
    /**
     * MV
     */
    public static final byte MV = 6;
    
    public static final Map<Byte, String> PIC_TYPE_KEY = Map.of(PicTypeConstant.MUSIC,
            "music",
            PicTypeConstant.PLAYLIST,
            "playlist",
            PicTypeConstant.ALBUM,
            "album",
            PicTypeConstant.ARTIST,
            "artist",
            PicTypeConstant.USER_AVATAR,
            "userAvatar",
            PicTypeConstant.USER_BACKGROUND,
            "userBackground");
    
    public static final Map<String, Byte> PIC_TYPE_VALUE = Map.of("music",
            PicTypeConstant.MUSIC,
            "playlist",
            PicTypeConstant.PLAYLIST,
            "album",
            PicTypeConstant.ALBUM,
            "artist",
            PicTypeConstant.ARTIST,
            "userAvatar",
            PicTypeConstant.USER_AVATAR,
            "userBackground",
            PicTypeConstant.USER_BACKGROUND);
    
    public static final Map<Byte, Byte> tagToPic = Map.of(
            TargetTagConstant.TARGET_MV_TAG, PicTypeConstant.MV,
            TargetTagConstant.TARGET_MUSIC_GENRE, PicTypeConstant.MUSIC,
            TargetTagConstant.TARGET_MUSIC_TAG, PicTypeConstant.MUSIC,
            TargetTagConstant.TARGET_ALBUM_GENRE, PicTypeConstant.ALBUM,
            TargetTagConstant.TARGET_COLLECT_TAG, PicTypeConstant.PLAYLIST
    );
    
    private PicTypeConstant() {
    }
}
