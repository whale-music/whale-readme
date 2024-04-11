package org.api.admin.model.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.mybatis.model.convert.MusicConvert;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileAlbumDetailRes {
    
    @Schema(title = "专辑表ID")
    private Long id;
    
    @Schema(title = "专辑名")
    private String albumName;
    
    @Schema(title = "专辑版本（比如录音室版，现场版）")
    private String subType;
    
    @Schema(title = "专辑简介")
    private String description;
    
    @Schema(title = "发行公司")
    private String company;
    
    @Schema(title = "专辑发布时间")
    private LocalDateTime publishTime;
    
    @Schema(title = "上传用户ID")
    private Long userId;
    
    @Schema(name = "封面地址")
    private String picUrl;
    
    @Schema(name = "音乐数据")
    private List<Music> musicList = new ArrayList<>();
    
    
    public void setMusicList(Collection<MusicConvert> musicList) {
        this.musicList = musicList.parallelStream().map(Music::new).toList();
    }
    
    @Data
    public static class Music implements Serializable {
        private Long id;
        private String musicName;
        private String aliasName;
        private Integer timeLength;
        public Music(MusicConvert m) {
            this.id = m.getId();
            this.musicName = m.getMusicName();
            this.aliasName = m.getAliasName();
            this.timeLength = m.getTimeLength();
        }
    }
}
