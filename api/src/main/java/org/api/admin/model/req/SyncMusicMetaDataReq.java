package org.api.admin.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncMusicMetaDataReq {
    /**
     * 资源ID
     */
    private Long resourceId;
    /**
     * 封面地址与封面文件base64互斥
     */
    private String picUrl;
    /**
     * 封面文件base64与封面地址互斥
     */
    private String picBase64;
    /**
     * 音乐名
     */
    private String musicName;
    /**
     * 逗号分割,
     */
    private String musicAliasName;
    /**
     * 逗号分割: ,
     */
    private String musicArtist;
    /**
     * 专辑歌手
     */
    private String albumArtist;
    /**
     * 专辑名
     */
    private String albumName;
    /**
     * 发布日期
     */
    private String year;
    /**
     * 歌词
     */
    private String lyric;
    /**
     * 流派
     */
    private String genre;
    
    /**
     * 标签
     */
    private String tag;
}
