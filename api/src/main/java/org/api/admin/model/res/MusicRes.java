package org.api.admin.model.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbAlbumPojo;
import org.core.mybatis.pojo.TbArtistPojo;
import org.core.mybatis.pojo.TbMusicPojo;
import org.core.mybatis.pojo.TbResourcePojo;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MusicRes extends TbMusicPojo {
    @Schema(name = "歌手")
    private List<TbArtistPojo> singerList;
    
    @Schema(name = "专辑名")
    private TbAlbumPojo album;
    
    @Schema(name = "歌曲地址")
    private List<TbResourcePojo> musicUrlList;
    
    @Schema(name = "是否可以播放")
    private Boolean isPlaying;
}
